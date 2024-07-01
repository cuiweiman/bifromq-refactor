package com.zachary.bifromq.basekv.raft.functest;

import com.zachary.bifromq.basekv.raft.exception.CompactionException;
import com.zachary.bifromq.basekv.raft.functest.annotation.Cluster;
import com.zachary.bifromq.basekv.raft.functest.annotation.Config;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestListener;
import com.zachary.bifromq.basekv.raft.functest.template.SharedRaftConfigTestTemplate;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.basekv.raft.exception.DropProposalException.OVERRIDDEN;
import static com.zachary.bifromq.basekv.raft.exception.DropProposalException.THROTTLED_BY_THRESHOLD;
import static com.google.protobuf.ByteString.EMPTY;
import static com.google.protobuf.ByteString.copyFromUtf8;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
@Listeners(RaftGroupTestListener.class)
public class ProposeTest extends SharedRaftConfigTestTemplate {
    @Test(groups = "integration")
    public void testProposalOverridden1() {
        testProposalOverridden(true);
    }

    @Config(preVote = false)
    @Test(groups = "integration")
    public void testProposalOverridden2() {
        testProposalOverridden(true);
    }

    @Test(groups = "integration")
    public void testProposalOverridden3() {
        testProposalOverridden(false);
    }

    @Config(preVote = false)
    @Test(groups = "integration")
    public void testProposalOverridden4() {
        testProposalOverridden(false);
    }

    private void testProposalOverridden(boolean compaction) {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        group.propose(leader, copyFromUtf8("appCommand1"));
        group.propose(leader, copyFromUtf8("appCommand2"));
        group.propose(leader, copyFromUtf8("appCommand3"));
        assertTrue(group.awaitIndexCommitted(group.currentFollowers().get(0), 4));
        assertTrue(group.awaitIndexCommitted(group.currentFollowers().get(1), 4));

        log.info("Isolate {}", leader);
        group.isolate(leader);
        // following 3 entries are un-commit
        CompletableFuture<Void> propose5Future = group.propose(leader, copyFromUtf8("appCommand4")); // <- 5
        CompletableFuture<Void> propose6Future = group.propose(leader, copyFromUtf8("appCommand5")); // <- 6
        group.propose(leader, copyFromUtf8("appCommand6")); // <- 7
        group.propose(leader, copyFromUtf8("appCommand7")); // <- 8
        await().until(() -> group.currentLeader().isPresent() && !leader.equals(group.currentLeader().get()));
        group.await(200); // enough ticks to let old leader self step down
        String newLeader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(newLeader, 5));
        log.info("New leader {} elected", newLeader);
        assertNotEquals(newLeader, leader);
        // propose two more entries via new leader and wait for committed
        group.propose(newLeader, copyFromUtf8("appCommandA"));
        group.propose(newLeader, copyFromUtf8("appCommandB"));
        assertTrue(group.awaitIndexCommitted(newLeader, 7));
        // make a compaction and propose more
        if (compaction) {
            group.compact(newLeader, EMPTY, 7);
        }
        group.propose(newLeader, copyFromUtf8("appCommandC"));
        group.propose(newLeader, copyFromUtf8("appCommandD"));
        assertTrue(group.awaitIndexCommitted(newLeader, 9));
        // integrate old leader, and trigger install snapshot
        log.info("Integrate {}", leader);
        group.integrate(leader);
        assertTrue(group.awaitIndexCommitted(leader, 9));
        Assert.assertEquals(group.logEntries(leader, 8), group.logEntries(newLeader, 8));
        // the uncommitted proposal on old leader will be failed with Overridden exception
        try {
            propose5Future.get();
        } catch (Exception e) {
            assertEquals(e.getCause(), OVERRIDDEN);
        }
        try {
            propose6Future.get();
        } catch (Exception e) {
            assertEquals(e.getCause(), OVERRIDDEN);
        }
    }

    @Cluster(v = "V1")
    @Test(groups = "integration")
    public void testSingleNodePropose() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));

        group.propose(leader, copyFromUtf8("appCommand1"));
        assertTrue(group.awaitIndexCommitted(leader, 2));

        group.propose(leader, copyFromUtf8("appCommand2"));
        group.propose(leader, copyFromUtf8("appCommand3"));
        assertTrue(group.awaitIndexCommitted(leader, 4));

        List<LogEntry> entries = group.retrieveCommitted(leader, 2, -1);
        Optional<LogEntry> entry4 = group.entryAt(leader, 4);
        assertEquals(entry4.get().getData(), copyFromUtf8("appCommand3"));
        assertEquals(entries.size(), 3);
        assertEquals(entries.get(0).getData(), copyFromUtf8("appCommand1"));
        assertEquals(entries.get(1).getData(), copyFromUtf8("appCommand2"));
        assertEquals(entries.get(2).getData(), copyFromUtf8("appCommand3"));
    }

    @Test(groups = "integration")
    public void testProposeFromLeader() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));

        try {
            // propose from leader
            group.propose(leader, copyFromUtf8("appCommand1")).join();
            await().until(() -> 2 == group.commitIndex(leader));
            Assert.assertEquals(group.retrieveCommitted(leader, 2, -1).get(0).getData(),
                copyFromUtf8("appCommand1"));
            group.propose(leader, copyFromUtf8("appCommand2")).join();
            await().until(() -> 3 == group.commitIndex(leader));
            Assert.assertEquals(group.retrieveCommitted(leader, 3, -1).get(0).getData(),
                copyFromUtf8("appCommand2"));
            group.propose(leader, copyFromUtf8("appCommand3")).join();
            await().until(() -> 4 == group.commitIndex(leader));
            Assert.assertEquals(group.retrieveCommitted(leader, 4, -1).get(0).getData(),
                copyFromUtf8("appCommand3"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test(groups = "integration")
    public void testProposeFromFollower() {
        assertTrue(group.awaitIndexCommitted("V1", 1));
        assertTrue(group.awaitIndexCommitted("V2", 1));
        assertTrue(group.awaitIndexCommitted("V3", 1));

        String follower = group.currentFollowers().get(0);
        try {
            group.propose(follower, copyFromUtf8("appCommand1")).join();
            Assert.assertEquals(group.commitIndex(group.currentLeader().get()), 2);
            assertTrue(group.awaitIndexCommitted("V1", 2));
            assertTrue(group.awaitIndexCommitted("V2", 2));
            assertTrue(group.awaitIndexCommitted("V3", 2));
            Assert.assertEquals(group.retrieveCommitted(follower, 2, -1).get(0).getData(),
                copyFromUtf8("appCommand1"));

            group.propose(follower, copyFromUtf8("appCommand2")).join();
            Assert.assertEquals(group.commitIndex(group.currentLeader().get()), 3);
            group.propose(follower, copyFromUtf8("appCommand3")).join();
            Assert.assertEquals(group.commitIndex(group.currentLeader().get()), 4);
            group.propose(follower, copyFromUtf8("appCommand4")).join();
            Assert.assertEquals(group.commitIndex(group.currentLeader().get()), 5);
            assertTrue(group.awaitIndexCommitted("V1", 5));
            assertTrue(group.awaitIndexCommitted("V2", 5));
            assertTrue(group.awaitIndexCommitted("V3", 5));

            Assert.assertEquals(group.retrieveCommitted(follower, 5, -1).get(0).getData(),
                copyFromUtf8("appCommand4"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(groups = "integration")
    public void testProposeThrottled() {
        assertTrue(group.awaitIndexCommitted("V1", 1));
        assertTrue(group.awaitIndexCommitted("V2", 1));
        assertTrue(group.awaitIndexCommitted("V3", 1));
        String leader = group.currentLeader().get();
        log.info("Leader {} elected", leader);

        for (int i = 0; i < 1100; ++i) {
            group.propose(leader, copyFromUtf8(("appCommand-" + i)));
        }
        try {
            group.propose(leader, copyFromUtf8("appCommand-10")).join();
        } catch (Exception e) {
            assertSame(e.getCause(), THROTTLED_BY_THRESHOLD);
        }
    }

    @Test(groups = "integration")
    public void testCompaction() {
        assertTrue(group.awaitIndexCommitted("V1", 1));
        assertTrue(group.awaitIndexCommitted("V2", 1));
        assertTrue(group.awaitIndexCommitted("V3", 1));
        String leader = group.currentLeader().get();
        log.info("Leader {} elected", leader);

        for (int i = 0; i < 10; ++i) {
            group.propose(leader, copyFromUtf8(("appCommand-" + i))).join();
        }
        group.compact(leader, ByteString.EMPTY, 5).join();
        try {
            group.entryAt(leader, 5);
            fail();
        } catch (Throwable e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
        assertTrue(group.entryAt(leader, 6).isPresent());
        try {
            group.compact(leader, ByteString.EMPTY, 12).join();
        } catch (Exception e) {
            assertTrue(e.getCause() == CompactionException.STALE_SNAPSHOT);
        }
        try {
            group.compact(leader, ByteString.EMPTY, 4).join();
        } catch (Exception e) {
            assertTrue(e.getCause() == CompactionException.STALE_SNAPSHOT);
        }
    }
}
