package com.insta2phase.services.twoPhaseCommit;

import com.insta2phase.crudQueries.Query;

import com.insta2phase.utils.log.PrimaryLog;
import org.springframework.transaction.annotation.Transactional;



public interface TwoPhaseCommitService {
    @PrimaryLog
    @Transactional
    <E> boolean twoPhaseCommit(Query query, Class<E> entityClass);
}
