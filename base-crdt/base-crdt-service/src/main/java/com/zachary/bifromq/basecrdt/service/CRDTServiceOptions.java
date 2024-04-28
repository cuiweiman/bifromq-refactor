package com.zachary.bifromq.basecrdt.service;

import com.zachary.bifromq.basecrdt.store.CRDTStoreOptions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder(toBuilder = true)
@Accessors(chain = true, fluent = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CRDTServiceOptions {

    @Builder.Default
    CRDTStoreOptions storeOptions = new CRDTStoreOptions();

}
