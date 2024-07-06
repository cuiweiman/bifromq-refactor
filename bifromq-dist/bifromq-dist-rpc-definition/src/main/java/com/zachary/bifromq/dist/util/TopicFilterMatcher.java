package com.zachary.bifromq.dist.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TopicFilterMatcher {
    private final TopicTrie topicTrie;
    private final TopicFilterTrieIterator iterator;

    public TopicFilterMatcher(TopicTrie root) {
        topicTrie = root;
        iterator = new TopicFilterTrieIterator(root);
    }

    public Optional<Map<String, Iterable<TopicMessage>>> match(String escapedTopicFilter) {
        List<String> filterLevels = parse(escapedTopicFilter);
        Map<String, Iterable<TopicMessage>> matchedTopics = new HashMap<>();
        match(0, filterLevels, topicTrie.root(), matchedTopics);
        return matchedTopics.isEmpty() ? Optional.empty() : Optional.of(matchedTopics);
    }

    private void match(int matchingLevel, List<String> filterLevels, TrieNode node,
                       Map<String, Iterable<TopicMessage>> matchedTopics) {
        String filterName = filterLevels.get(matchingLevel);
        if (matchingLevel + 1 < filterLevels.size() && filterLevels.get(matchingLevel + 1).equals("#")) {
            // # match parent level as well. [MQTT-4.7.1-2]
            if (node.isLastTopicLevel()) {
                // current node matches real topic
                filterLevels.set(matchingLevel, node.levelName());
                matchedTopics.put(TopicUtil.fastJoin("/", filterLevels.subList(1, matchingLevel + 1)),
                    node.messages());
                filterLevels.set(matchingLevel, filterName);
            }
        }
        switch (filterName) {
            case "#":
                if (matchingLevel == 1 && node.levelName().startsWith(TopicUtil.SYS_PREFIX)) {
                    // system topic should not be matched by first "#". [MQTT-4.7.2-1]
                    break;
                }
                // '#' matches current node, so we replacing it with actual level name
                filterLevels.set(matchingLevel, node.levelName());
                if (node.isLastTopicLevel()) {
                    // current node matches real topic
                    matchedTopics.put(TopicUtil.fastJoin("/", filterLevels.subList(1, filterLevels.size())),
                        node.messages());
                }
                // append a # for matching next level
                filterLevels.add("#");
                for (TrieNode childNode : node.children()) {
                    match(matchingLevel + 1, filterLevels, childNode, matchedTopics);
                }
                filterLevels.set(matchingLevel, "#");
                filterLevels.remove(matchingLevel + 1);
                break;
            case "+":
                if (matchingLevel == 1 && node.levelName().startsWith(TopicUtil.SYS_PREFIX)) {
                    // system topic should not be matched by first "+". [MQTT-4.7.2-1]
                    break;
                }
                // '+' matches current node, so we replacing it with actual level name
                filterLevels.set(matchingLevel, node.levelName());
                if (matchingLevel + 1 == filterLevels.size()) {
                    // if no more level to match and current node matches real topic
                    if (node.isLastTopicLevel()) {
                        matchedTopics.put(TopicUtil.fastJoin("/", filterLevels.subList(1, filterLevels.size())),
                            node.messages());
                    }
                } else {
                    // else there are more levels to match
                    for (TrieNode childNode : node.children()) {
                        match(matchingLevel + 1, filterLevels, childNode, matchedTopics);
                    }
                }
                filterLevels.set(matchingLevel, "+");
                break;
            default:
                if (filterName.equals(node.levelName())) {
                    if (matchingLevel + 1 == filterLevels.size()) {
                        // if no more level to match and current node matches real topic
                        if (node.isLastTopicLevel()) {
                            // current node matches real topic
                            matchedTopics.put(TopicUtil.fastJoin("/", filterLevels.subList(1, filterLevels.size())),
                                node.messages());
                        }
                    } else {
                        // else there are more levels to match
                        for (TrieNode childNode : node.children()) {
                            match(matchingLevel + 1, filterLevels, childNode, matchedTopics);
                        }
                    }
                }
        }
    }

    public Optional<String> higher(String escapedTopicFilter) {
        iterator.forward(escapedTopicFilter);
        while (iterator.hasNext()) {
            String topicFilter = iterator.next();
            if (topicFilter.compareTo(escapedTopicFilter) > 0) {
                return Optional.of(topicFilter);
            }
        }
        return Optional.empty();
    }

    private static List<String> parse(String escapedTopicFilter) {
        List<String> topicLevels = new ArrayList<>();
        topicLevels.add(TopicUtil.NUL); // always starts with NUL
        char splitter = '\u0000';
        StringBuilder tl = new StringBuilder();
        for (int i = 0; i < escapedTopicFilter.length(); i++) {
            if (escapedTopicFilter.charAt(i) == splitter) {
                topicLevels.add(tl.toString());
                tl.delete(0, tl.length());
            } else {
                tl.append(escapedTopicFilter.charAt(i));
            }
        }
        topicLevels.add(tl.toString());
        return topicLevels;
    }

}
