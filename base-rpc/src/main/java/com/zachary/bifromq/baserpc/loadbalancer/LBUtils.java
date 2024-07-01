package com.zachary.bifromq.baserpc.loadbalancer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/29 17:25
 */
public class LBUtils {

    static class Tuple<X> {
        final Integer weight;
        final X x;

        private Tuple(Integer weight, X x) {
            this.weight = weight;
            this.x = x;
        }

        static <X> Tuple of(Integer weight, X x) {
            return new Tuple(weight, x);
        }

        @Override
        public String toString() {
            return String.format("weight: %d, object: %s", weight, x.toString());
        }
    }


    static <T> List<T> toWeightedRRSequence(List<Tuple<T>> tuples) {
        List<T> sequence = new ArrayList<>();
        int i = -1;
        int n = tuples.size();
        int currentW = 0;
        List<Integer> weights = tuples.stream().map(tuple -> tuple.weight).collect(Collectors.toList());
        if (!weights.isEmpty()) {
            int maxW = Collections.max(weights);
            int gcdW = getGCD(weights);
            while (true) {
                i = (i + 1) % n;
                if (i == 0) {
                    currentW = currentW - gcdW;
                    if (currentW < 0) {
                        currentW = maxW;
                    }
                    if (currentW == 0) {
                        break;
                    }
                }
                if (weights.get(i) >= currentW) {
                    sequence.add(tuples.get(i).x);
                }
            }
        }
        return sequence;
    }


    private static int getGCD(List<Integer> ints) {
        int n = ints.size();
        int result = ints.get(0);
        for (int i = 1; i < n; i++) {
            result = gcd(ints.get(i), result);
        }
        return result;
    }

    private static int gcd(int a, int b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

}
