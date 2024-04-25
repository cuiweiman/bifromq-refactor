
package com.zachary.bifromq.basecrdt.store;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICausalCRDT;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessage;
import io.reactivex.rxjava3.core.Observable;
import lombok.NonNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ICRDTStore {
    /**
     * Construct a new instance
     *
     * @param options
     * @return new instance
     */
    static ICRDTStore newInstance(@NonNull CRDTStoreOptions options) {
        return new CRDTStore(options);
    }

    /**
     * The global unique id of the store.
     * 全局唯一ID
     * <br>
     * NOTE. To ensure the uniqueness in distributed deployment, make sure the time is synchronized with each
     * other(clock skew below 1S), and start the stores in serial with sufficient delay(at least 1 second).
     *
     * @return
     */
    long id();

    /**
     * Host a CRDT replica if not host yet. The replica will be assigned a internal generated globally unique id.
     *
     * @param crdtURI the name of the CRDT
     * @return the logical address
     */
    Replica host(String crdtURI);

    /**
     * Host a CRDT replica using the specified replicaId. It's caller's duty to ensure the uniqueness of the provided id
     * within the external managed cluster.
     *
     * @param crdtURI
     * @param replicaId
     * @return
     */
    Replica host(String crdtURI, ByteString replicaId);

    /**
     * Stop hosting the replica for given uri
     *
     * @param uri
     */
    CompletableFuture<Void> stopHosting(String uri);

    /**
     * Returns an iterator to iterate all CRDT replicas currently hosting
     *
     * @return the iterator from CRDTName to a set of logical replica addresses
     */
    Iterator<Replica> hosting();

    /**
     * Get the live replica
     *
     * @param uri the id of the hosted replica
     * @return the replica object of the CRDT
     */
    <T extends ICausalCRDT> Optional<T> get(String uri);

    /**
     * Join a cluster from specified local address. Some remote members will be selected to be the neighbors with which
     * the local replica keeps synchronizing.
     *
     * @param localAddr the local addr
     * @param cluster   the list of member address
     */
    void join(String uri, ByteString localAddr, Set<ByteString> cluster);

    /**
     * Currently bind address of hosted replica in join cluster
     *
     * @return
     */
    Optional<ByteString> localAddr(String uri);

    /**
     * Currently joined cluster of given hosted replica
     *
     * @return
     */
    Optional<Set<ByteString>> cluster(String uri);

    /**
     * If the peer addr is the neighbor of the local replica in current cluster landscape, trigger a full sync with it
     *
     * @param uri
     * @param peerAddr
     */
    void sync(String uri, ByteString peerAddr);

    /**
     * An observable of messages originated from current store
     *
     * @return
     */
    Observable<CRDTStoreMessage> storeMessages();

    /**
     * Start the store by providing observable of incoming store messages
     * <br>
     * NOTE: the messages with toStoreId set to '0' is used for broadcast, and will be accepted by every CRDTStore
     *
     * @param replicaMessages the observable to receive messages from others
     * @return
     */
    void start(Observable<CRDTStoreMessage> replicaMessages);

    /**
     * Stop the store
     */
    void stop();
}
