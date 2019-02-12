package com.insta2phase.utils.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.controllers.responses.CanCommit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Aspect
public class LogAspect {

    @Value("${server.port}")
    private String port;

    private Log log;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init(){
        log = LogFactory.getLog(LogAspect.class);
    }

    @Before("@annotation(com.insta2phase.utils.log.PrimaryLog)")
    public void beforePrimary(JoinPoint jp) {
        String targetClassName = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();

        log.info("PRIMARY:: called " + targetClassName + "." + methodName + "()");
    }

    @AfterReturning(value = "@annotation(com.insta2phase.utils.log.PrimaryLog)", returning = "returned")
    public void afterPrimary(JoinPoint jp, Object returned) {
        try {
            String targetClassName = jp.getTarget().getClass().getSimpleName();
            String methodName = jp.getSignature().getName();

            log.info("PRIMARY::" + targetClassName + "." + methodName + "() returned:" + objectMapper.writeValueAsString(returned));
        }catch (Exception ex){
            log.error("PRIMARY:: Exception during " + jp.getSignature());
        }
    }

    @Before("@annotation(com.insta2phase.utils.log.ReplicaLog)")
    public void beforeReplica(JoinPoint jp) {
        String targetClassName = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();

        log.info("REPLICA:: called " + targetClassName + "." + methodName + "()");
    }

    @AfterReturning(value = "@annotation(com.insta2phase.utils.log.ReplicaLog)", returning = "returned")
    public void afterReplica(JoinPoint jp, Object returned) {
        try {
            String targetClassName = jp.getTarget().getClass().getSimpleName();
            String methodName = jp.getSignature().getName();

            log.info("REPLICA::" + targetClassName + "." + methodName + "() returned:" + objectMapper.writeValueAsString(returned));
        }catch (Exception ex){
            log.error("REPLICA:: Exception during " + jp.getSignature());
        }
    }

    @Before("@annotation(com.insta2phase.utils.log.InitPhaseLog)")
    public void beforeInitPhase(JoinPoint jp){
        String message = null;
        try {
            message = "BEGIN COMMIT " + objectMapper.writeValueAsString(jp.getArgs());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info(message);
    }

    @AfterReturning(value = "@annotation(com.insta2phase.utils.log.InitPhaseLog)", returning = "returned")
    public void afterInitPhase(JoinPoint jp, Object returned) {
        String message = null;
        try {
            message = "RESULT VOTES: " + objectMapper.writeValueAsString(returned);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(message);
    }

    @Before("@annotation(com.insta2phase.utils.log.CommitPhaseLog)")
    public void beforeCommitPhase(JoinPoint jp) {
        CanCommit canCommit = (CanCommit) jp.getArgs()[1];

        String message = canCommit.name();
        log.info(message);
    }
}
