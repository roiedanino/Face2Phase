package com.insta2phase.controllers.participantAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.controllers.responses.Vote;
import com.insta2phase.crudQueries.Query;
import com.insta2phase.services.participant.ParticipantService;
import com.insta2phase.utils.StringUtils;
import com.insta2phase.utils.log.ReplicaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/participant")
public class TwoPhaseCommitParticipantAPIImplementation implements TwoPhaseCommitParticipantAPI {

    private ParticipantService participantService;


    @Autowired
    public TwoPhaseCommitParticipantAPIImplementation(ParticipantService participantService) {
        setParticipantService(participantService);
    }

    @ReplicaLog
    @PostMapping(path = "/{type}/{operation}")
    @Override
    public Vote initPhase(@PathVariable("type") String type, @PathVariable("operation") String crudOperation, @RequestBody String request) {
        System.err.println("INIT PHASE: " + type + "\n" + crudOperation +"\n" + request);

        Query query = toQuery(type, crudOperation, request);

        return participantService.initPhase(type, crudOperation, query);
    }

    @ReplicaLog
    @GetMapping(path = "/commit/{requestId}")
    @Override
    public Object commitPhase(@PathVariable("requestId") String requestId) {
        System.err.println("COMMIT PHASE: " + requestId);

        return participantService.commitPhase(requestId);
    }

    @ReplicaLog
    @GetMapping(path = "/abort/{requestId}")
    @Override
    public Object abortPhase(@PathVariable("requestId") String requestId) {
        System.err.println("COMMIT PHASE: " + requestId);

        return participantService.abortPhase(requestId);
    }

    private void setParticipantService(ParticipantService participantService) {
        this.participantService = participantService;
    }

    private Query toQuery(String type, String crudOperationStr, String queryBody) {
        try {
            String typePascalCase = StringUtils.toPascalCase(type);
            String crudOperationPascalCase = StringUtils.toPascalCase(crudOperationStr);
            ObjectMapper objectMapper = new ObjectMapper();

            Class<Query> queryClass = (Class<Query>) Class.forName("com.insta2phase.crudQueries." + crudOperationPascalCase
                    + typePascalCase + "Query");

            return objectMapper.readValue(queryBody, queryClass);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
