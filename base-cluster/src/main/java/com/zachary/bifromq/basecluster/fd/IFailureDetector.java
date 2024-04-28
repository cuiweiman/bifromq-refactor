package com.zachary.bifromq.basecluster.fd;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Timed;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface IFailureDetector {

    Duration baseProbeInterval();

    Duration baseProbeTimeout();

    /**
     * Start the failure detector using a specified member selector
     *
     * @param targetSelector
     */
    void start(IProbingTargetSelector targetSelector);

    /**
     * Stop the failure detector
     */
    CompletableFuture<Void> shutdown();

    /**
     * Hot observable of probe-succeed target
     *
     * @return
     */
    Observable<Timed<IProbingTarget>> succeeding();

    /**
     * Hot observable of probe-fail target
     *
     * @return
     */
    Observable<Timed<IProbingTarget>> suspecting();

    void penaltyHealth();

    /**
     * Hot observable of scoring the local health based on failure detecting process
     *
     * @return
     */
    Observable<Integer> healthScoring();
}
