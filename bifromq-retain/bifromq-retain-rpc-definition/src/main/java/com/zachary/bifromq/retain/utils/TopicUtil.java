package com.zachary.bifromq.retain.utils;

import java.util.ArrayList;
import java.util.List;

public class TopicUtil {
    public static final String NUL = String.valueOf('\u0000');
    public static final char TOPIC_SEPARATOR = '/';

    public static boolean isWildcardTopicFilter(String topicFilter) {
        return topicFilter.contains("+") || topicFilter.contains("#");
    }

    public static String escape(String topicFilter) {
        assert !topicFilter.contains(NUL);
        return topicFilter.replace("/", NUL);
    }

    public static boolean match(List<String> topicLevels, List<String> matchLevels) {
        boolean matchMultiLevel = matchLevels.get(matchLevels.size() - 1).equals("#");
        if (!matchMultiLevel && topicLevels.size() != matchLevels.size()) {
            return false;
        }
        for (int i = 0; i < topicLevels.size(); i++) {
            String matchLevel = matchLevels.get(i);
            switch (matchLevel) {
                case "+":
                    continue;
                case "#":
                    return true;
                default:
                    if (!matchLevel.equals(topicLevels.get(i))) {
                        return false;
                    }
            }
        }
        return true;
    }

    public static List<String> parse(String topic, boolean isEscaped) {
        // parse a topic or topic filter string into a list of topic levels
        // eg. "/" -> ["",""], "/a" -> ["",a], "a/" -> [a,""]
        List<String> topicLevels = new ArrayList<>();
        char splitter = isEscaped ? '\u0000' : TOPIC_SEPARATOR;
        StringBuilder tl = new StringBuilder();
        for (int i = 0; i < topic.length(); i++) {
            if (topic.charAt(i) == splitter) {
                topicLevels.add(tl.toString());
                tl.delete(0, tl.length());
            } else {
                tl.append(topic.charAt(i));
            }
        }
        topicLevels.add(tl.toString());
        return topicLevels;
    }
}
