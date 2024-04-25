package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.core.api.IRWORSet;
import com.zachary.bifromq.basecrdt.core.api.operation.RWORSetOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.rworset;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class RWORSetTest extends CRDTTest {
    private final Replica leftReplica = Replica.newBuilder()
        .setUri(toURI(rworset, "rworset"))
        .setId(ByteString.copyFromUtf8("left-address"))
        .build();
    private final Replica rightReplica = Replica.newBuilder()
        .setUri(toURI(rworset, "rworset"))
        .setId(ByteString.copyFromUtf8("right-address"))
        .build();
    private final ByteString elem1 = ByteString.copyFromUtf8("e1");
    private final ByteString elem2 = ByteString.copyFromUtf8("e2");
    private final ByteString elem3 = ByteString.copyFromUtf8("e3");

    @Test
    public void testOperation() {
        RWORSetInflater rworSetInflater =
            new RWORSetInflater(0, leftReplica, newStateLattice(leftReplica.getId(), 1000),
                executor, Duration.ofMillis(100));
        IRWORSet rworSet = rworSetInflater.getCRDT();
        assertEquals(rworSet.id(), leftReplica);

        assertTrue(rworSet.isEmpty());
        assertFalse(rworSet.elements().hasNext());

        rworSet.execute(RWORSetOperation.add(elem1)).join();
        assertFalse(rworSet.isEmpty());
        assertTrue(rworSet.contains(elem1));
        TestUtil.assertSame(Sets.<ByteString>newHashSet(elem1).iterator(), rworSet.elements());

        rworSet.execute(RWORSetOperation.remove(elem1)).join();
        assertTrue(rworSet.isEmpty());
        assertFalse(rworSet.contains(elem1));
        assertFalse(rworSet.elements().hasNext());

        rworSet.execute(RWORSetOperation.add(elem1));
        rworSet.execute(RWORSetOperation.add(elem2));
        rworSet.execute(RWORSetOperation.add(elem3)).join();
        assertTrue(rworSet.contains(elem1));
        assertTrue(rworSet.contains(elem2));
        assertTrue(rworSet.contains(elem3));

        rworSet.execute(RWORSetOperation.clear());
        rworSet.execute(RWORSetOperation.add(elem1)).join();
        assertTrue(rworSet.contains(elem1));
        TestUtil.assertSame(Sets.<ByteString>newHashSet(elem1).iterator(), rworSet.elements());
    }

    @Test
    public void testJoin() {
        RWORSetInflater leftInflater = new RWORSetInflater(0, leftReplica,
            newStateLattice(leftReplica.getId(), 1000), executor, Duration.ofMillis(100));
        IRWORSet left = leftInflater.getCRDT();

        RWORSetInflater rightInflater = new RWORSetInflater(1, rightReplica,
            newStateLattice(rightReplica.getId(), 1000), executor, Duration.ofMillis(100));
        IRWORSet right = rightInflater.getCRDT();

        left.execute(RWORSetOperation.add(elem1));
        left.execute(RWORSetOperation.add(elem2));
        left.execute(RWORSetOperation.add(elem3)).join();

        sync(leftInflater, rightInflater);
        assertTrue(right.contains(elem1));
        assertTrue(right.contains(elem2));
        assertTrue(right.contains(elem3));

        // remove win
        left.execute(RWORSetOperation.add(elem1)).join();
        right.execute(RWORSetOperation.remove(elem1)).join();
        sync(rightInflater, leftInflater);
        assertFalse(right.contains(elem1));
    }
}
