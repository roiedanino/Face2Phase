package com.insta2phase.services.log;

import com.insta2phase.entities.TwoPhaseLog;
import reactor.core.publisher.Flux;

public interface TwoPhaseLogService {
    TwoPhaseLog log(TwoPhaseLog twoPhaseLog);

    TwoPhaseLog log(String title, String content);

    void logAsCoordinator(String title, String content);

    void logAsParticipant(String title, String content);

    Flux<TwoPhaseLog> getAllLogsFlux();
}
