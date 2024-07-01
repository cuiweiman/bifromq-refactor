package com.zachary.bifromq.basekv.raft.functest;

import com.zachary.bifromq.basekv.raft.functest.annotation.Cluster;
import com.zachary.bifromq.basekv.raft.functest.annotation.Config;
import com.zachary.bifromq.basekv.raft.functest.annotation.Ticker;
import com.zachary.bifromq.basekv.raft.functest.template.RaftGroupTestListener;
import com.zachary.bifromq.basekv.raft.functest.template.SharedRaftConfigTestTemplate;
import com.zachary.bifromq.basekv.raft.proto.LogEntry;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.basekv.raft.exception.ReadIndexException.COMMIT_INDEX_NOT_CONFIRMED;
import static com.zachary.bifromq.basekv.raft.exception.ReadIndexException.LEADER_STEP_DOWN;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

@Slf4j
@Listeners(RaftGroupTestListener.class)
public class ReadIndexTest extends SharedRaftConfigTestTemplate {

    @Test(groups = "integration")
    public void testReadIndexWithLeaderLease() {
        String leader = group.currentLeader().get();

        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted("V1", 3));
        assertTrue(group.awaitIndexCommitted("V2", 3));
        assertTrue(group.awaitIndexCommitted("V3", 3));

        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 3;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Test(groups = "integration")
    public void testReadIndexFromFollower() {
        String leader = group.currentLeader().get();

        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted("V1", 3));
        assertTrue(group.awaitIndexCommitted("V2", 3));
        assertTrue(group.awaitIndexCommitted("V3", 3));

        await().until(() -> {
            try {
                return group.readIndex(group.currentFollowers().get(0)).join() >= 3;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Cluster(v = "V1")
    @Test(groups = "integration")
    public void testReadIndexInSingleNodeCluster() {
        String leader = group.currentLeader().get();

        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 1;
            } catch (Exception e) {
                return false;
            }
        });

        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted("V1", 3));

        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 3;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Ticker(disable = true)
    @Test(groups = "integration")
    public void testReadIndexWithoutCommitIndexConfirmed() {
        group.run(10, TimeUnit.MILLISECONDS);
        await().until(() -> group.currentLeader().isPresent());
        String leader = group.currentLeader().get();

        try {
            long commitIndex = group.readIndex(leader).join();
            // readIndex may be successful
            Optional<LogEntry> logEntry = group.entryAt(leader, commitIndex);
            assertTrue(logEntry.isPresent());
            assertEquals(logEntry.get().getTypeCase(), LogEntry.TypeCase.CONFIG);
        } catch (Throwable e) {
            assertSame(e.getCause(), COMMIT_INDEX_NOT_CONFIRMED);
        }
    }

    @Config(readOnlyLeaderLeaseMode = false)
    @Test(groups = "integration")
    public void testReadIndexWithoutLeaderLease() {
        String leader = group.currentLeader().get();
        assertTrue(group.awaitIndexCommitted(leader, 1));
        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 1;
            } catch (Exception e) {
                return false;
            }
        });

        // log entry of appCommand1 won't be committed immediately
        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));

        assertTrue(group.awaitIndexCommitted(leader, 2));
        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 2;
            } catch (Exception e) {
                return false;
            }
        });

        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand3"));
        assertTrue(group.awaitIndexCommitted(leader, 4));

        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 4;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Config(readOnlyLeaderLeaseMode = false)
    @Test(groups = "integration")
    public void testReadIndexWithoutLeaderLeaseByRejectedAppendReplies() {
        String leader = group.currentLeader().get();
        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 1;
            } catch (Exception e) {
                return false;
            }
        });

        String follower1 = group.currentFollowers().get(0);
        String follower2 = group.currentFollowers().get(1);
        group.isolate(follower2);
        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted(leader, 3));
        assertTrue(group.awaitIndexCommitted(follower1, 3));
        group.compact(leader, ByteString.copyFromUtf8("appSMSnapshot"), 3).join();

        group.recoverNetwork();
        group.isolate(follower1);

        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 3;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Config(readOnlyLeaderLeaseMode = false)
    @Test(groups = "integration")
    public void testReadIndexWhenLeaderLostQuorum() {
        String leader = group.currentLeader().get();
        await().until(() -> {
            try {
                return group.readIndex(leader).join() >= 1;
            } catch (Exception e) {
                return false;
            }
        });

        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted(leader, 3));
        group.isolate(leader);

        group.readIndex(leader)
            .handle((r, e) -> {
                assertEquals(e, LEADER_STEP_DOWN);
                return CompletableFuture.completedFuture(null);
            }).join();
    }

    @Test(groups = "integration")
    public void testReadIndexWhenTransferLeadershipInLeaseMode() {
        String leader = group.currentLeader().get();
        String follower = group.currentFollowers().get(0);

        group.propose(leader, ByteString.copyFromUtf8("appCommand1"));
        group.propose(leader, ByteString.copyFromUtf8("appCommand2"));
        assertTrue(group.awaitIndexCommitted("V1", 3));
        assertTrue(group.awaitIndexCommitted("V2", 3));
        assertTrue(group.awaitIndexCommitted("V3", 3));
        log.info("Transfer leadership to {}", follower);

        group.transferLeadership(leader, follower);
        log.info("Isolate leader {}", leader);
        group.isolate(leader);

        group.readIndex(leader)
            .handle((r, e) -> {
                assertEquals(e, LEADER_STEP_DOWN);
                return CompletableFuture.completedFuture(null);
            }).join();

        await().until(() -> {
            try {
                return group.readIndex(follower).join() >= 4;
            } catch (Exception e) {
                // continue to await when catch COMMIT_INDEX_NOT_CONFIRMED
                return false;
            }
        });
    }
}
