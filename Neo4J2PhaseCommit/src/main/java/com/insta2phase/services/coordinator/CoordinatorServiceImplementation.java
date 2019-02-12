package com.insta2phase.services.coordinator;

import com.insta2phase.crudQueries.Query;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.services.twoPhaseCommit.TwoPhaseCommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CoordinatorServiceImplementation implements CoordinatorService{
    private ConfigurableApplicationContext spring;
    private TwoPhaseCommitService twoPhaseCommitService;

    @Autowired
    public CoordinatorServiceImplementation(ConfigurableApplicationContext spring, TwoPhaseCommitService twoPhaseCommitService) {
        setSpring(spring);
        setTwoPhaseCommitService(twoPhaseCommitService);
    }

    @Transactional
    @Override
    public <T> Optional<T> executeQuery(Query query, Class<T> clazz){
        TwoPhaseCrudOperation<Query> twoPhaseCrudOperation = parseTwoPhaseOperation(query, clazz);

        boolean canExecute = twoPhaseCrudOperation.canExecute(query);

        if(canExecute){
            boolean succeed = twoPhaseCommitService.twoPhaseCommit(query, clazz);

            if(succeed){
                return Optional.of((T)(twoPhaseCrudOperation.execute()));
            }
        }

        return Optional.empty();
    }

    private TwoPhaseCrudOperation<Query> parseTwoPhaseOperation(Query query, Class<?> clazz){
        try {
            String queryName = query.getClass().getSimpleName();
            String componentName = "com.insta2phase.crudServices."+clazz.getSimpleName().toLowerCase() +
                    ".TwoPhase" + queryName.substring(0,queryName.indexOf("Query"));
            return (TwoPhaseCrudOperation<Query>) spring.getBean(Class.forName(componentName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void setSpring(ConfigurableApplicationContext spring) {
        this.spring = spring;
    }

    private void setTwoPhaseCommitService(TwoPhaseCommitService twoPhaseCommitService) {
        this.twoPhaseCommitService = twoPhaseCommitService;
    }
}
