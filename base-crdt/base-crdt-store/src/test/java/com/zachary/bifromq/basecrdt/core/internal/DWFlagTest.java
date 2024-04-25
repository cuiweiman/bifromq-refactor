
package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IDWFlag;
import com.zachary.bifromq.basecrdt.core.api.operation.DWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.dwflag;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Slf4j
public class DWFlagTest extends CRDTTest {
    private final Replica leftReplica = Replica.newBuilder()
            .setUri(toURI(dwflag, "dwflag"))
            .setId(ByteString.copyFromUtf8("left-address"))
            .build();
    private final Replica rightReplica = Replica.newBuilder()
            .setUri(toURI(dwflag, "dwflag"))
            .setId(ByteString.copyFromUtf8("right-address"))
            .build();

    @Test
    public void testOperation() {
        DWFlagInflater dwFlagInflater = new DWFlagInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 1000), executor, Duration.ofMillis(100));
        IDWFlag dwFlag = dwFlagInflater.getCRDT();
        Assert.assertEquals(dwFlag.id(), leftReplica);

        assertTrue(dwFlag.read());
        dwFlag.execute(DWFlagOperation.disable()).join();
        assertFalse(dwFlag.read());

        dwFlag.execute(DWFlagOperation.enable());
        dwFlag.execute(DWFlagOperation.disable()).join();
        assertFalse(dwFlag.read());

        dwFlag.execute(DWFlagOperation.enable()).join();
        assertTrue(dwFlag.read());
    }

    @Test
    public void testJoin() {
        DWFlagInflater leftInflater = new DWFlagInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 1000000),
                executor, Duration.ofMillis(100));
        IDWFlag left = leftInflater.getCRDT();

        DWFlagInflater rightInflater = new DWFlagInflater(1, rightReplica,
                newStateLattice(rightReplica.getId(), 1000000),
                executor, Duration.ofMillis(100));
        IDWFlag right = rightInflater.getCRDT();

        left.execute(DWFlagOperation.disable()).join();
        right.execute(DWFlagOperation.enable()).join();
        // nothing to send from right to left
        assertFalse(rightInflater
                .delta(leftInflater.latticeEvents(), leftInflater.historyEvents(), 10).join().isPresent());
        // something to send from left to right
        rightInflater.join(leftInflater
                .delta(rightInflater.latticeEvents(), rightInflater.historyEvents(), 10).join().get()).join();

        assertFalse(left.read());
        Assert.assertEquals(right.read(), left.read());

        right.execute(DWFlagOperation.enable()).join();
        assertTrue(right.read());

        sync(leftInflater, rightInflater);
        assertTrue(left.read());
        Assert.assertEquals(right.read(), left.read());
    }
}
