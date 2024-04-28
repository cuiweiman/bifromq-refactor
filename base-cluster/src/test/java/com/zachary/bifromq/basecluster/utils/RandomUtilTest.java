package com.zachary.bifromq.basecluster.utils;

import com.zachary.bifromq.basecluster.util.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static org.testng.Assert.assertTrue;

public class RandomUtilTest {
    @Test
    public void pickFromEmpty() {
        assertTrue(RandomUtils.uniqueRandomPickAtMost(emptyList(), 0, i -> true).isEmpty());
        assertTrue(RandomUtils.uniqueRandomPickAtMost(emptyList(), 5, i -> true).isEmpty());
    }

    @Test
    public void pickNothing() {
        assertTrue(RandomUtils.uniqueRandomPickAtMost(generateList(10), 0, i -> true).isEmpty());
    }

    @Test
    public void pickAll() {
        List<Integer> orig = generateList(10);
        Assert.assertEquals(orig, RandomUtils.uniqueRandomPickAtMost(orig, 10, i -> true));
        Assert.assertEquals(orig, RandomUtils.uniqueRandomPickAtMost(orig, 20, i -> true));
    }

    @Test
    public void pickMatch() {
        List<Integer> orig = generateList(10);
        Assert.assertEquals(5, RandomUtils.uniqueRandomPickAtMost(orig, 10, i -> i % 2 == 0).size());
        assertTrue(RandomUtils.uniqueRandomPickAtMost(orig, 3, i -> i % 2 == 0).size() <= 3);
    }

    private List<Integer> generateList(int size) {
        List<Integer> intList = new ArrayList<>(size);
        IntStream.range(0, size).forEach(intList::add);
        Collections.shuffle(intList);
        return intList;
    }
}
