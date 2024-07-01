package com.zachary.bifromq.dist.util;

import com.google.common.collect.AbstractIterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LinkedMap;

import javax.annotation.CheckForNull;
import java.util.Iterator;

import static com.zachary.bifromq.dist.util.TopicUtil.NUL;
import static com.zachary.bifromq.dist.util.TopicUtil.fastJoin;

@Slf4j
public class TopicFilterTrieIterator extends AbstractIterator<String> {
    private final TopicFilterTrie root;
    private final LinkedMap<TopicFilterTrie, Iterator<TopicFilterTrie>> current;

    public TopicFilterTrieIterator(TopicTrie topicTrie) {
        root = TopicFilterTrie.build(topicTrie);
        this.current = new LinkedMap<>();
        this.current.put(root, root.children());
    }

    public void forward(String escapedTopicFilter) {
        root.forward(TopicUtil.parse(escapedTopicFilter, true));
        this.current.clear();
        this.current.put(root, root.children());
    }

    @CheckForNull
    @Override
    protected String computeNext() {
        findNext();
        if (current.isEmpty()) {
            return endOfData();
        }
        return fastJoin(NUL, current.keySet(), TopicFilterTrie::levelName).substring(2);
    }

    private void findNext() {
        if (current.isEmpty()) {
            return;
        }
        TopicFilterTrie node = current.lastKey();
        Iterator<TopicFilterTrie> childItr = current.get(node);
        if (childItr.hasNext()) {
            TopicFilterTrie child = childItr.next();
            current.put(child, child.children());
            if (!child.isMatchTopic()) {
                findNext();
            }
        } else {
            current.remove(node);
            findNext();
        }
    }
}
