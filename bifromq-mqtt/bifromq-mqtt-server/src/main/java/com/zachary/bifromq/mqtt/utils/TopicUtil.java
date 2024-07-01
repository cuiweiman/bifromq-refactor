package com.zachary.bifromq.mqtt.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicUtil {
    private static final String UNORDERED_SHARE = "$share/";
    private static final String ORDERED_SHARE = "$oshare/";

    public static final char TOPIC_SEPARATOR = '/';
    public static final char NULL = '\u0000';
    public static final char SINGLE_WILDCARD = '+';
    public static final char MULTIPLE_WILDCARD = '#';

    public static boolean isValidTopic(String topic, int maxLevelLength, int maxLevel, int maxLength) {
        assert maxLength <= 65535 && maxLevelLength <= maxLength;
        if (topic.isEmpty() || topic.length() > maxLength) {
            // [MQTT-4.7.3-1]
            return false;
        }
        if (topic.startsWith(ORDERED_SHARE) || topic.startsWith(UNORDERED_SHARE)) {
            return false;
        }
        StringBuilder tlsb = new StringBuilder();
        int level = 1;
        for (int i = 0; i < topic.length(); i++) {
            if (topic.charAt(i) == TOPIC_SEPARATOR) {
                if (++level > maxLevel) {
                    return false;
                }
                if (tlsb.length() > maxLevelLength) {
                    return false;
                }
                tlsb.delete(0, tlsb.length());
            } else {
                char c = topic.charAt(i);
                if (c == NULL || c == SINGLE_WILDCARD || c == MULTIPLE_WILDCARD) {
                    // [MQTT-4.7.3-2] and [MQTT-4.7.1-1]
                    return false;
                }
                tlsb.append(c);
            }
        }
        return tlsb.length() <= maxLevelLength;
    }

    public static boolean isValidTopicFilter(String topicFilter, int maxLevelLength, int maxLevel, int maxLength) {
        // TODO: could be optimized further by building a FSM
        if (topicFilter.startsWith(UNORDERED_SHARE)) {
            maxLength += UNORDERED_SHARE.length();
        }
        if (topicFilter.startsWith(ORDERED_SHARE)) {
            maxLength += ORDERED_SHARE.length();
        }
        assert maxLength <= 65535 && maxLevelLength <= maxLength;
        if (topicFilter.isEmpty() || topicFilter.length() > maxLength) {
            // [MQTT-4.7.3-1]
            return false;
        }
        int i = 0;
        StringBuilder tlsb = new StringBuilder();
        if (topicFilter.startsWith(ORDERED_SHARE) || topicFilter.startsWith(UNORDERED_SHARE)) {
            // validate share name
            for (i = topicFilter.indexOf(TOPIC_SEPARATOR) + 1; i < topicFilter.length(); i++) {
                char c = topicFilter.charAt(i);
                if (c == TOPIC_SEPARATOR) {
                    break;
                }
                if (c == MULTIPLE_WILDCARD || c == SINGLE_WILDCARD || c == NULL) {
                    // [MQTT-4.8.2-2]
                    return false;
                }
                tlsb.append(c);
            }
            if (tlsb.length() == 0) {
                // [MQTT-4.8.2-1]
                return false;
            }
            if (i == topicFilter.length()) {
                // [MQTT-4.8.2-2]
                return false;
            }
            tlsb.delete(0, tlsb.length());
            // skip one separator to real topicFilter start pos
            i++;
        }
        int startIdx = i;
        int level = 1;
        for (; i < topicFilter.length(); i++) {
            if (topicFilter.charAt(i) == TOPIC_SEPARATOR) {
                if (++level > maxLevel) {
                    return false;
                }
                if (tlsb.length() > maxLevelLength) {
                    return false;
                }
                tlsb.delete(0, tlsb.length());
            } else {
                char c = topicFilter.charAt(i);
                if (c == NULL) {
                    // [MQTT-4.7.3-2]
                    return false;
                }
                if (c == MULTIPLE_WILDCARD) {
                    if (i != topicFilter.length() - 1) {
                        return false;
                    }
                    if (i != startIdx && topicFilter.charAt(i - 1) != TOPIC_SEPARATOR) {
                        return false;
                    }
                }
                if (c == SINGLE_WILDCARD) {
                    if (i == startIdx) {
                        if (i != topicFilter.length() - 1 && topicFilter.charAt(i + 1) != TOPIC_SEPARATOR) {
                            return false;
                        }
                    } else if (i == topicFilter.length() - 1) {
                        if (topicFilter.charAt(i - 1) != TOPIC_SEPARATOR) {
                            return false;
                        }
                    } else {
                        if (topicFilter.charAt(i - 1) != TOPIC_SEPARATOR
                            || topicFilter.charAt(i + 1) != TOPIC_SEPARATOR) {
                            return false;
                        }
                    }

                }
                tlsb.append(c);
            }
        }
        if (level > maxLevel) {
            return false;
        }
        return tlsb.length() <= maxLevelLength;
    }

    public static String parseTopicFilter(String topicFilter) {
        // must be valid topic filter
        if (topicFilter.startsWith(ORDERED_SHARE) || topicFilter.startsWith(UNORDERED_SHARE)) {
            // validate share name
            int i;
            for (i = topicFilter.indexOf(TOPIC_SEPARATOR) + 1; i < topicFilter.length(); i++) {
                char c = topicFilter.charAt(i);
                if (c == TOPIC_SEPARATOR) {
                    break;
                }
            }
            return topicFilter.substring(i + 1);
        }
        return topicFilter;
    }
}
