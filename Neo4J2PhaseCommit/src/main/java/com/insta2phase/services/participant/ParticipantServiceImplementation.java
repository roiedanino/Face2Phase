package com.insta2phase.services.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.controllers.responses.CanCommit;
import com.insta2phase.controllers.responses.Vote;
import com.insta2phase.crudQueries.Query;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;

import com.insta2phase.dal.utils.IdGeneratorService;
import com.insta2phase.services.log.TwoPhaseLogService;
import com.insta2phase.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParticipantServiceImplementation implements ParticipantService {

    private ConfigurableApplicationContext spring;
    private IdGeneratorService idGeneratorService;
    private TwoPhaseLogService twoPhaseLogService;
    private Map<String, TwoPhaseCrudOperation<Query>> operationsMap;


    @Autowired
    public ParticipantServiceImplementation(ConfigurableApplicationContext spring, IdGeneratorService idGeneratorService, TwoPhaseLogService twoPhaseLogService) {
        setSpring(spring);
        setIdGeneratorService(idGeneratorService);
        setTwoPhaseLogService(twoPhaseLogService);
        operationsMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Vote initPhase(String type, String crudOperation, Query query) {
        String componentName = "com.insta2phase.crudServices." + type.toLowerCase() + ".TwoPhase"
                + StringUtils.toPascalCase(crudOperation)
                + StringUtils.toPascalCase(type);

        twoPhaseLogService.logAsParticipant("Init Phase Request: "
                + StringUtils.toPascalCase(crudOperation) + " "
                + StringUtils.toPascalCase(type), toJSON(query));

        try {
            TwoPhaseCrudOperation<Query> twoPhaseCrudOperation =
                    (TwoPhaseCrudOperation<Query>)spring.getBean(Class.forName(componentName));

            boolean canExecute = twoPhaseCrudOperation.canExecute(query);
            CanCommit canCommit = canExecute ? CanCommit.COMMIT : CanCommit.ABORT;
            String operationId = idGeneratorService.nextId();
            operationsMap.put(operationId, twoPhaseCrudOperation);

            twoPhaseLogService.logAsParticipant("Init Phase "
                    + StringUtils.toPascalCase(crudOperation) + " "
                    + StringUtils.toPascalCase(type) +" Response", canCommit.name() + ", operation id: " + operationId);

            return new Vote(canCommit, operationId);

        } catch (ClassNotFoundException e) {
            twoPhaseLogService.logAsParticipant("Init Phase Error: "
                    + StringUtils.toPascalCase(crudOperation) + " "
                    + StringUtils.toPascalCase(type),
                    "Invalid operation: " + type + crudOperation + query + e.getMessage());

            throw new RuntimeException("Invalid operation: " + type + crudOperation + query + e);
        }
    }

    @Override
    public Object commitPhase(String requestId) {
        if(!operationsMap.containsKey(requestId)){
            twoPhaseLogService.logAsParticipant("Commit Phase Error", "No such request id: " + requestId);
            throw new RuntimeException("No such request id: " + requestId);
        }

        twoPhaseLogService.logAsParticipant("Committed Successfully", "Request Id: " + requestId);
        return operationsMap.get(requestId).execute();
    }

    @Override
    public Object abortPhase(String requestId) {
        if(!operationsMap.containsKey(requestId)){
            twoPhaseLogService.logAsParticipant("Abort Phase Error", "No such request id: " + requestId);
            throw new RuntimeException("No such request id: " + requestId);
        }

        twoPhaseLogService.logAsParticipant("Aborted Successfully", "Request Id: " + requestId);
        return operationsMap.remove(requestId);
    }

    private String toJSON(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "toJSON Failed " + object + ": " + e.getMessage();
        }
    }

    private void setSpring(ConfigurableApplicationContext spring) {
        this.spring = spring;
    }

    private void setIdGeneratorService(IdGeneratorService idGeneratorService) {
        this.idGeneratorService = idGeneratorService;
    }

    private void setTwoPhaseLogService(TwoPhaseLogService twoPhaseLogService) {
        this.twoPhaseLogService = twoPhaseLogService;
    }
}
