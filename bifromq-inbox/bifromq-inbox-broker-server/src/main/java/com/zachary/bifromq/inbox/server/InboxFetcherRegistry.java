package com.zachary.bifromq.inbox.server;

import com.google.common.collect.Iterators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.zachary.bifromq.metrics.TenantMeter.gauging;
import static com.zachary.bifromq.metrics.TenantMeter.stopGauging;
import static com.zachary.bifromq.metrics.TenantMetric.InboxFetcherGauge;

final class InboxFetcherRegistry implements Iterable<IInboxQueueFetcher> {
    // how do we handle multiple fetchers under same (tenantId, inboxId, qos) combination which may happen when
    // "persistent session" clients kicking each other
    // delivererKey+tenantId -> inboxId -> InboxFetcher
    private final NavigableMap<String, Map<String, IInboxQueueFetcher>> fetchers = new ConcurrentSkipListMap<>();

    void reg(IInboxQueueFetcher fetcher) {
        fetchers.compute(fetcher.delivererKey() + fetcher.tenantId(), (key, val) -> {
            if (val == null) {
                val = new HashMap<>();
                gauging(fetcher.tenantId(), InboxFetcherGauge,
                    () -> fetchers.getOrDefault(fetcher.tenantId(), Collections.EMPTY_MAP).size());
            }
            val.put(fetcher.inboxId(), fetcher);
            return val;
        });
    }

    void unreg(IInboxQueueFetcher fetcher) {
        fetchers.compute(fetcher.delivererKey() + fetcher.tenantId(), (tenantId, m) -> {
            if (m != null) {
                m.remove(fetcher.inboxId(), fetcher);
                if (m.size() == 0) {
                    stopGauging(fetcher.tenantId(), InboxFetcherGauge);
                    return null;
                }
            }
            return m;
        });
    }

    boolean has(String tenantId, String inboxId, String delivererKey) {
        return fetchers.getOrDefault(delivererKey + tenantId, Collections.emptyMap()).containsKey(inboxId);
    }

    IInboxQueueFetcher get(String tenantId, String inboxId, String delivererKey) {
        return fetchers.getOrDefault(delivererKey + tenantId, Collections.emptyMap()).get(inboxId);
    }

    void signalFetch(String delivererKey) {
        SortedMap<String, Map<String, IInboxQueueFetcher>> subMap = fetchers.tailMap(delivererKey);
        for (String key : subMap.keySet()) {
            if (key.startsWith(delivererKey)) {
                return;
            }
            for (IInboxQueueFetcher fetcher : subMap.get(key).values()) {
                fetcher.signalFetch();
            }
        }
    }

    @Override
    public Iterator<IInboxQueueFetcher> iterator() {
        return Iterators.concat(fetchers.values().stream().map(m -> m.values().iterator()).iterator());
    }
}
