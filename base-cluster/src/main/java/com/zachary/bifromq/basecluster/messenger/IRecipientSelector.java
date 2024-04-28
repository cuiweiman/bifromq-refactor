package com.zachary.bifromq.basecluster.messenger;


import java.util.Collection;

public interface IRecipientSelector {
    /**
     * Select at most <limit> recipient randomly
     *
     * @param limit
     * @return
     */
    Collection<? extends IRecipient> selectForSpread(int limit);

    /**
     * The estimated cluster size currently
     *
     * @return
     */
    int clusterSize();
}
