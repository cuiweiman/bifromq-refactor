package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICCounter;
import com.zachary.bifromq.basecrdt.core.api.operation.CCounterOperation;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.Replica;
import io.reactivex.rxjava3.observers.TestObserver;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Optional;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.aworset;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.cctr;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CCounterTest extends CRDTTest {
    private final Replica leftReplica = Replica.newBuilder()
            .setUri(toURI(cctr, "ccounter"))
            .setId(ByteString.copyFromUtf8("left-address"))
            .build();
    private final Replica rightReplica = Replica.newBuilder()
            .setUri(toURI(aworset, "ccounter"))
            .setId(ByteString.copyFromUtf8("right-address"))
            .build();

    @Test
    public void testOperation() {
        CCounterInflater cctrInflater = new CCounterInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 1000), executor, Duration.ofMillis(100));
        ICCounter cctr = cctrInflater.getCRDT();
        assertEquals(cctr.id(), leftReplica);

        assertEquals(cctr.read(), 0);

        cctr.execute(CCounterOperation.add(10)).join();
        assertEquals(cctr.read(), 10);
        cctr.execute(CCounterOperation.add(10)).join();
        assertEquals(cctr.read(), 20);

        cctr.execute(CCounterOperation.preset(10)).join();
        assertEquals(cctr.read(), 10);

        cctr.execute(CCounterOperation.zeroOut()).join();
        assertEquals(cctr.read(), 0);

        // batch execute
        cctr.execute(CCounterOperation.add(10));
        cctr.execute(CCounterOperation.zeroOut());
        cctr.execute(CCounterOperation.add(10)).join();
        assertEquals(cctr.read(), 10);
    }

    @Test
    public void testJoin() {
        CCounterInflater leftInflater = new CCounterInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter left = leftInflater.getCRDT();

        CCounterInflater rightInflater = new CCounterInflater(1, rightReplica,
                newStateLattice(rightReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter right = rightInflater.getCRDT();

        left.execute(CCounterOperation.add(10));
        left.execute(CCounterOperation.add(10));
        left.execute(CCounterOperation.add(10)).join();

        sync(leftInflater, rightInflater);
        assertEquals(right.read(), 30);

        // following codes simulate OR history broadcast during anti-entropy
        TestObserver<Long> inflationObserver = new TestObserver<>();
        right.inflation().subscribe(inflationObserver);
        right.execute(CCounterOperation.add(50));
        right.execute(CCounterOperation.zeroOut()).join();
        assertTrue(inflationObserver.values().isEmpty());
        sync(leftInflater, rightInflater);
        assertEquals(left.read(), 30);
        assertEquals(right.read(), 30);
    }

    @Test
    public void testZeroOut() {
        CCounterInflater leftInflater = new CCounterInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter left = leftInflater.getCRDT();

        CCounterInflater rightInflater = new CCounterInflater(1, rightReplica,
                newStateLattice(rightReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter right = rightInflater.getCRDT();

        left.execute(CCounterOperation.add(10));
        left.execute(CCounterOperation.add(10));
        left.execute(CCounterOperation.add(10)).join();

        sync(leftInflater, rightInflater);
        assertEquals(right.read(), 30);

        // after partition left contribution is discarded locally but don't keep the removal history
        right.execute(CCounterOperation.zeroOut(leftReplica.getId())).join();
        assertEquals(right.read(), 0);

        // sync again will recover left-contributed state in right replica
        left.execute(CCounterOperation.add(10)).join();
        assertEquals(left.read(), 40);
        sync(leftInflater, rightInflater);
        assertEquals(right.read(), 40);
    }

    @Test
    public void testZeroOutInBatch() {
        CCounterInflater leftInflater = new CCounterInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter left = leftInflater.getCRDT();

        CCounterInflater rightInflater = new CCounterInflater(1, rightReplica,
                newStateLattice(rightReplica.getId(), 100000), executor, Duration.ofMillis(100));
        ICCounter right = rightInflater.getCRDT();

        left.execute(CCounterOperation.add(10)).join();

        sync(leftInflater, rightInflater);
        assertEquals(right.read(), 10);

        left.execute(CCounterOperation.add(10)).join();
        Optional<Iterable<Replacement>> deltaProto =
                leftInflater.delta(
                        rightInflater.latticeEvents(),
                        rightInflater.historyEvents(),
                        Integer.MAX_VALUE).join();
        rightInflater.join(deltaProto.get()).join();
        right.execute(CCounterOperation.zeroOut(leftReplica.getId())).join();

        assertEquals(right.read(), 0);
        // sync again will recover left-contributed state in right replica
        left.execute(CCounterOperation.add(10)).join();
        sync(leftInflater, rightInflater);
        assertEquals(left.read(), 30);
        assertEquals(right.read(), 30);
    }
}
