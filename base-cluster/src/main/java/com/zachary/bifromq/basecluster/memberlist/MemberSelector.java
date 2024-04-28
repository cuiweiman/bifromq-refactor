package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.messenger.IRecipient;
import com.zachary.bifromq.basecluster.messenger.IRecipientSelector;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MemberSelector implements IRecipientSelector {
    private final Disposable disposable;
    private volatile List<IRecipient> recipients = new ArrayList<>();

    public MemberSelector(IHostMemberList memberList, Scheduler scheduler, IHostAddressResolver addressResolver) {
        disposable = memberList.members()
                .observeOn(scheduler)
                .subscribe(members -> recipients = members.keySet()
                        .stream()
                        .map(m -> {
                            InetSocketAddress address = addressResolver.resolve(m);
                            return (IRecipient) () -> address;
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public Collection<? extends IRecipient> selectForSpread(int limit) {
        List<IRecipient> addresses = recipients;
        if (addresses.size() <= limit) {
            return addresses;
        }
        Collections.shuffle(addresses);
        return addresses.subList(0, limit);
    }

    @Override
    public int clusterSize() {
        return recipients.size();
    }

    public void stop() {
        disposable.dispose();
    }
}
