package com.zachary.bifromq.baserpc.loadbalancer;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

class ConsistentHashRouter<T> {

    /**
     * TreeMap 是 有序的 Map，非线程安全
     */
    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private final HashFunction hashFunction;
    private final KeyFunction<T> keyFunction;

    interface HashFunction {
        long hash(String key);
    }

    interface KeyFunction<T> {
        String getKey(T node);
    }

    static class VirtualNode<T> {
        final T physicalNode;
        final int replicaIndex;
        final KeyFunction<T> keyFunction;
        final String pNodeKey;
        final String vNodekey;

        VirtualNode(T physicalNode, int replicaIndex, KeyFunction<T> keyFunc) {
            this.replicaIndex = replicaIndex;
            this.physicalNode = physicalNode;
            this.keyFunction = keyFunc;
            pNodeKey = keyFunction.getKey(physicalNode);
            vNodekey = pNodeKey + "-" + replicaIndex;
        }

        public String getKey() {
            return vNodekey;
        }

        boolean isVirtualNodeOf(T pNode) {
            return pNodeKey.equals(keyFunction.getKey(pNode));
        }

        T getPhysicalNode() {
            return physicalNode;
        }
    }

    ConsistentHashRouter(Collection<T> pNodes, KeyFunction<T> keyFunction, int vNodeCount) {
        this(pNodes, keyFunction, vNodeCount, new Murmur3());
    }

    ConsistentHashRouter(Collection<T> pNodes, KeyFunction<T> keyFunction, int vNodeCount, HashFunction hashFunction) {
        this.keyFunction = keyFunction;
        this.hashFunction = hashFunction;
        if (pNodes != null) {
            for (T pNode : pNodes) {
                int weight = ((Map.Entry<String, Integer>) pNode).getValue();
                addNode(pNode, vNodeCount * weight);
            }
        }
    }

    void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) {
            throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
        }
        int existingReplicas = getExistingReplicas(pNode);
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas, keyFunction);
            ring.put(hashFunction.hash(vNode.getKey()), vNode);
        }
    }

    T routeNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashVal = hashFunction.hash(objectKey);
        SortedMap<Long, VirtualNode<T>> tailMap = ring.tailMap(hashVal);
        Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashVal).getPhysicalNode();
    }

    boolean isEmpty() {
        return ring.isEmpty();
    }

    int getExistingReplicas(T pNode) {
        int replicas = 0;
        for (VirtualNode<T> vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicas++;
            }
        }
        return replicas;
    }

    private static class Murmur3 implements HashFunction {
        com.google.common.hash.HashFunction hash;

        Murmur3() {
            hash = Hashing.murmur3_128();
        }

        @Override
        public long hash(String key) {
            HashCode code = hash.hashString(key, Charsets.UTF_8);
            return code.asLong();
        }
    }
}
