package com.zachary.bifromq.basecluster.fd;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.messenger.IRecipient;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:06
 */
public interface IProbingTarget extends IRecipient {
    ByteString id();
}
