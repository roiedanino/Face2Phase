package com.insta2phase.services.log;

import com.insta2phase.dal.AccountDao;
import com.insta2phase.dal.TwoPhaseLogDao;
import com.insta2phase.entities.TwoPhaseLog;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TwoPhaseLogServiceImplementation implements TwoPhaseLogService{
    private TwoPhaseLogDao twoPhaseLogDao;
    private SubmissionPublisher<TwoPhaseLog> publisher;

    @Autowired
    public TwoPhaseLogServiceImplementation(TwoPhaseLogDao twoPhaseLogDao) {
        setTwoPhaseLogDao(twoPhaseLogDao);
        publisher = new SubmissionPublisher<>();
    }

    @Override
    public TwoPhaseLog log(TwoPhaseLog twoPhaseLog) {
        publisher.submit(twoPhaseLog);
        return twoPhaseLogDao.save(twoPhaseLog);
    }

    @Override
    public TwoPhaseLog log(String title, String content) {
        return twoPhaseLogDao.save(new TwoPhaseLog(title, content, new Date()));
    }

    public TwoPhaseLog log(String title, String content, TwoPhaseLog.Role role) {
        TwoPhaseLog twoPhaseLog = new TwoPhaseLog(title, content, new Date());
        twoPhaseLog.setRole(role);
        return log(twoPhaseLog);
    }

    @Override
    public void logAsCoordinator(String title, String content) {
        log(title, content, TwoPhaseLog.Role.Coordinator);
    }

    @Override
    public void logAsParticipant(String title, String content) {
        log(title, content, TwoPhaseLog.Role.Participant);
    }

    @Override
    public Flux<TwoPhaseLog> getAllLogsFlux() {
        //

        Lock lock = new ReentrantLock();
        Condition anyNewLogs = lock.newCondition();

        LogSubscriber logSubscriber = new LogSubscriber(lock, anyNewLogs);
        publisher.subscribe(logSubscriber);

        return Flux.create(emitter -> {
            twoPhaseLogDao.findAll().forEach(emitter::next);
            emitter.complete();
//            System.err.println("flux polling one more");
//            while (true) {
//                emitter.next(logSubscriber.next());
//            }
        });

//        ).map(i -> {
//            System.err.println("flux polling one more");
//
//        });
    }

    public void setTwoPhaseLogDao(TwoPhaseLogDao twoPhaseLogDao) {
        this.twoPhaseLogDao = twoPhaseLogDao;
    }
}

//
//        Iterator<TwoPhaseLog> iter =
//                twoPhaseLogDao.findAll(PageRequest.of(0, 10, Sort.Direction.ASC, "timestamp")).iterator();
//twoPhaseLogDao.findAll()