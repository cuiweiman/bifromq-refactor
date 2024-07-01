package com.zachary.bifromq.dist.util;

import com.zachary.bifromq.type.TopicMessage;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

import static com.zachary.bifromq.dist.util.TopicUtil.fastJoin;
import static java.util.Collections.singleton;

public interface TopicTrieIterator {
    static void iterate(TrieNode root, TopicTrieIterator iterator) {
        LinkedList<LinkedList<TrieNode>> toVisit = Lists.newLinkedList();
        toVisit.add(Lists.newLinkedList(singleton(root)));
        while (!toVisit.isEmpty()) {
            LinkedList<TrieNode> current = toVisit.poll();
            TrieNode lastTopicLevel = current.getLast();
            if (lastTopicLevel.isLastTopicLevel()) {
                TrieNode first = current.removeFirst();
                iterator.next(fastJoin("/", current, TrieNode::levelName), lastTopicLevel.messages());
                current.addFirst(first);
            }
            List<TrieNode> children = lastTopicLevel.children();
            for (int i = children.size() - 1; i >= 0; i--) {
                LinkedList<TrieNode> next = Lists.newLinkedList(current);
                next.addLast(children.get(i));
                toVisit.addFirst(next);
            }
        }
    }

    void next(String topic, Iterable<TopicMessage> messages);
}
