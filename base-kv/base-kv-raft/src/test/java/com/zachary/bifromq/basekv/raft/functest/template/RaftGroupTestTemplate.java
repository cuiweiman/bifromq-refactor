package com.zachary.bifromq.basekv.raft.functest.template;

import com.zachary.bifromq.basekv.raft.functest.annotation.Cluster;
import com.zachary.bifromq.basekv.raft.proto.ClusterConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class RaftGroupTestTemplate {
    private final ClusterConfig defaultClusterConfig =
        ClusterConfig.newBuilder().addVoters("V1").addVoters("V2").addVoters("V3").build();
    private ClusterConfig clusterConfigInUse;

    public void createClusterByAnnotation(Method testMethod) {
        Cluster cluster = testMethod.getAnnotation(Cluster.class);
        clusterConfigInUse = cluster == null ? defaultClusterConfig : build(cluster);
        startingTest(testMethod);
    }

    protected void startingTest(Method testMethod) {
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        clusterConfigInUse = null;
    }

    public final ClusterConfig clusterConfig() {
        return clusterConfigInUse;
    }

    private ClusterConfig build(Cluster c) {
        return ClusterConfig.newBuilder()
            .addAllVoters(toSet(c.v()))
            .addAllLearners(toSet(c.l()))
            .addAllNextVoters(toSet(c.nv()))
            .addAllNextLearners(toSet(c.nl()))
            .build();
    }

    private Set<String> toSet(String commaSeparatedString) {
        commaSeparatedString = commaSeparatedString.trim();
        Set<String> set = new HashSet<>();
        if (!commaSeparatedString.isEmpty()) {
            set.addAll(Arrays.asList(commaSeparatedString.split(",")));
        }
        return set;
    }
}
