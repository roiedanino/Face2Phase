package com.insta2phase.services.log;

import com.insta2phase.entities.TwoPhaseLog;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Flow;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogSubscriber implements Flow.Subscriber<TwoPhaseLog> {
    private Flow.Subscription subscription;
    private Queue<TwoPhaseLog> logQueue = new LinkedList<>();

    private Lock lock;
    private Condition anyNewLogs;

    public LogSubscriber(Lock lock, Condition anyNewLogs){
        setLock(lock);
        setAnyNewLogs(anyNewLogs);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);

    }

    @Override
    public void onNext(TwoPhaseLog item) {
        lock.lock();
        logQueue.add(item);
        subscription.request(1);
        anyNewLogs.signalAll();
        lock.unlock();
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        subscription.cancel();
    }

    public boolean hasNext(){
        return !logQueue.isEmpty();
    }

    public TwoPhaseLog next(){
        try {
            lock.lock();

            if(!hasNext()) {
                System.err.println("AWAITING FOR SOME NEW LOGS");
                anyNewLogs.await();
                System.err.println("FINALLY WOKE UP WITH SOME NEW LOGS");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
        return logQueue.remove();
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public void setAnyNewLogs(Condition anyNewLogs) {
        this.anyNewLogs = anyNewLogs;
    }
}
