package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.IEWFlag;
import com.zachary.bifromq.basecrdt.core.api.operation.EWFlagOperation;
import com.zachary.bifromq.basecrdt.proto.Replica;
import io.reactivex.rxjava3.observers.TestObserver;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.zachary.bifromq.basecrdt.core.api.CRDTURI.toURI;
import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.ewflag;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Slf4j
public class EWFlagTest extends CRDTTest {
    private final Replica leftReplica = Replica.newBuilder()
            .setUri(toURI(ewflag, "ewflag"))
            .setId(ByteString.copyFromUtf8("left-address"))
            .build();
    private final Replica rightReplica = Replica.newBuilder()
            .setUri(toURI(ewflag, "ewflag"))
            .setId(ByteString.copyFromUtf8("right-address"))
            .build();

    @Test
    public void testOperation() {
        EWFlagInflater ewFlagInflater = new EWFlagInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 1000),
                executor, Duration.ofMillis(100));
        IEWFlag ewFlag = ewFlagInflater.getCRDT();
        Assert.assertEquals(ewFlag.id(), leftReplica);

        assertFalse(ewFlag.read());
        ewFlag.execute(EWFlagOperation.enable()).join();
        assertTrue(ewFlag.read());

        ewFlag.execute(EWFlagOperation.disable());
        ewFlag.execute(EWFlagOperation.enable()).join();
        assertTrue(ewFlag.read());

        ewFlag.execute(EWFlagOperation.disable()).join();
        assertFalse(ewFlag.read());
    }

    @Test
    public void testJoin() {
        EWFlagInflater leftInflater = new EWFlagInflater(0, leftReplica,
                newStateLattice(leftReplica.getId(), 1000000), executor, Duration.ofMillis(100));
        IEWFlag left = leftInflater.getCRDT();

        EWFlagInflater rightInflater = new EWFlagInflater(1, rightReplica,
                newStateLattice(rightReplica.getId(), 1000000), executor, Duration.ofMillis(100));
        IEWFlag right = rightInflater.getCRDT();

        left.execute(EWFlagOperation.enable()).join();
        right.execute(EWFlagOperation.disable()).join();
        sync(leftInflater, rightInflater);

        assertTrue(left.read());
        Assert.assertEquals(right.read(), left.read());

        TestObserver<Long> inflationObserver = new TestObserver<>();
        right.inflation().subscribe(inflationObserver);

        right.execute(EWFlagOperation.disable()).join();
        assertFalse(inflationObserver.values().isEmpty());
        sync(leftInflater, rightInflater);
        assertFalse(left.read());
        Assert.assertEquals(right.read(), left.read());
    }
}
