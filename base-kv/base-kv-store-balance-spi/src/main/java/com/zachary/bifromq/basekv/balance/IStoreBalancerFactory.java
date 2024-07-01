package com.zachary.bifromq.basekv.balance;

public interface IStoreBalancerFactory {

    StoreBalancer newBalancer(String localStoreId);

}
