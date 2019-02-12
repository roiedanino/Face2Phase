package com.insta2phase.controllers.participantAPI;


import com.insta2phase.controllers.responses.Vote;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface TwoPhaseCommitParticipantAPI {
    @PostMapping(path = "/{type}/{operation}")
    Vote initPhase(@PathVariable("type") String type, @PathVariable("operation") String crudOperation, @RequestBody String request) throws IOException;

    Object commitPhase(String requestId);

    @GetMapping(path = "/abort/{requestId}")
    Object abortPhase(@PathVariable("requestId") String requestId);
}
