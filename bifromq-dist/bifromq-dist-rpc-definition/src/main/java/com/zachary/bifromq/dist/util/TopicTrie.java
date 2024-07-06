package com.zachary.bifromq.dist.util;

import java.util.List;

public class TopicTrie {
    private int topicCount;
    private final TrieNode root;

    public TopicTrie() {
        root = new TrieNode();
    }

    public TrieNode root() {
        return root;
    }

    public void add(String topic, Iterable<TopicMessage> messages) {
        assert !topic.isEmpty();
        List<String> topicLevels = TopicUtil.parse(topic, false);
        if (root.add(topicLevels, messages)) {
            topicCount++;
        }
    }

    public int topicCount() {
        return topicCount;
    }
}
