package com.insta2phase.utils.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.controllers.responses.Vote;
import com.insta2phase.crudQueries.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public aspect AspectLogger {

    private Log log;
    private ObjectMapper objectMapper = new ObjectMapper();

    public AspectLogger(){
        log = LogFactory.getLog(AspectLogger.class);
    }

    pointcut initPhaseLogging(Query query, Class<?> entityClass) :
            execution(* Map<String, Vote> initPhase(Query, Class<?>) throws JsonProcessingException)
            && args(query, entityClass);

    before (Query query, Class<?> entityClass): initPhaseLogging(query, entityClass){
        String message = "INIT PHASE - BEGIN COMMIT - Entity:" + entityClass.getSimpleName() + ", Query: "+ query;
        log.info(message);
    }

    after (Query query, Class<?> entityClass) returning (Map<String, Vote> votes) : initPhaseLogging(query, entityClass){
        String message = "INIT PHASE - VOTES RESULTS - Entity:" + entityClass.getSimpleName() + ", Query: " + query + ", Votes: " + votes;
        log.info(message);
    }

//    pointcut commitPhaseLogging(Map<String, Vote> votes, Class<?> entityClass) :
//            execution(private void finalPhase(Map<String, Vote> votes, CanCommit canCommit))
//                    && args(query, entityClass);
}
