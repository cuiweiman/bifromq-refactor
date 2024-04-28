package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.messenger.IRecipient;
import com.google.common.util.concurrent.MoreExecutors;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class MemberSelectorTest {
    @Mock
    private IHostMemberList memberList;
    @Mock
    private IHostAddressResolver addressResolver;
    private PublishSubject<Map<HostEndpoint, Integer>> membersSubject = PublishSubject.create();
    private Scheduler scheduler = Schedulers.from(MoreExecutors.directExecutor());
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(memberList.members()).thenReturn(membersSubject);
        when(addressResolver.resolve(Fixtures.LOCAL_ENDPOINT)).thenReturn(Fixtures.LOCAL_ADDR);
        when(addressResolver.resolve(Fixtures.REMOTE_HOST_1_ENDPOINT)).thenReturn(Fixtures.REMOTE_ADDR_1);
    }

    @AfterMethod
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void noRecipients() {
        MemberSelector selector = new MemberSelector(memberList, scheduler, addressResolver);
        Collection<? extends IRecipient> recipients = selector.selectForSpread(10);
        assertTrue(recipients.isEmpty());
        assertTrue(selector.clusterSize() == 0);
    }

    @Test
    public void notEnoughRecipients() {
        MemberSelector selector = new MemberSelector(memberList, scheduler, addressResolver);
        membersSubject.onNext(new HashMap<>() {{
            put(Fixtures.LOCAL_ENDPOINT, 0);
            put(Fixtures.REMOTE_HOST_1_ENDPOINT, 0);
        }});
        Collection<? extends IRecipient> recipients = selector.selectForSpread(10);
        assertTrue(recipients.size() == 2);
        assertTrue(selector.clusterSize() == 2);
    }

    @Test
    public void selectRandomly() {
        MemberSelector selector = new MemberSelector(memberList, scheduler, addressResolver);
        membersSubject.onNext(new HashMap<>() {{
            put(Fixtures.LOCAL_ENDPOINT, 0);
            put(Fixtures.REMOTE_HOST_1_ENDPOINT, 0);
        }});
        await().until(() -> {
            Collection<? extends IRecipient> recipients = selector.selectForSpread(1);
            return recipients.size() == 1 && !recipients.stream().findFirst().get().addr().equals(Fixtures.LOCAL_ADDR);
        });
    }
}
