package com.insta2phase.services.participant;

import com.insta2phase.controllers.responses.Vote;
import com.insta2phase.crudQueries.Query;

public interface ParticipantService {
    Vote initPhase(String type, String crudOperation, Query query);

    Object commitPhase(String requestId);

    Object abortPhase(String requestId);
}
