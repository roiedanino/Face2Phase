package com.insta2phase.services.twoPhaseCommit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insta2phase.controllers.responses.CanCommit;
import com.insta2phase.controllers.responses.Vote;
import com.insta2phase.crudQueries.Query;

import com.insta2phase.services.log.TwoPhaseLogService;
import com.insta2phase.utils.log.PrimaryLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.stream.Stream;

import static com.insta2phase.controllers.responses.CanCommit.ABORT;
import static com.insta2phase.controllers.responses.CanCommit.COMMIT;

@Service
public class TwoPhaseCommitServiceImplementation implements TwoPhaseCommitService{

    @Value("${insta2Phase.replicas.urlList}")
    private String replicasUrls[];

    private Map<String, WebClient> webClients;
    private ExecutorService executorService;
    private TwoPhaseLogService twoPhaseLogService;

    @Autowired
    public TwoPhaseCommitServiceImplementation(TwoPhaseLogService twoPhaseLogService) {
        setTwoPhaseLogService(twoPhaseLogService);
    }

    @PostConstruct
    public void init(){
        webClients = new HashMap<>();
        if(replicasUrls != null && replicasUrls.length > 0) {
            executorService = Executors.newFixedThreadPool(replicasUrls.length);
        }
        System.err.println("REPLICAS ON: ");
        System.err.println(Arrays.toString(replicasUrls));
        twoPhaseLogService.logAsCoordinator("Replicas:", Arrays.toString(replicasUrls));


        ExchangeStrategies exchangeStrategies = ExchangeStrategies.withDefaults();
        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        Stream.of(replicasUrls).forEach(url -> {
            WebClient webClient = WebClient.builder()
                    .exchangeStrategies(exchangeStrategies)
                    .build();
            webClients.put(url, webClient);
        });
    }

    @Transactional
    @PrimaryLog
    @Override
    public <E> boolean twoPhaseCommit(Query query, Class<E> entityClass){

        Map<String, Vote> votes = null;
        try {
            twoPhaseLogService.logAsCoordinator("Beginning Init Phase", query.toString());
            votes = initPhase(query, entityClass);
            twoPhaseLogService.logAsCoordinator("After Init Phase, Votes are: ", votes.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return secondPhase(votes);
    }

    private Map<String, Vote> initPhase(Query query, Class<?> entityClass) throws JsonProcessingException {
        String entityTypeName = entityClass.getSimpleName();
        String operation = getCrudOperationName(query, entityClass);
        Map<String, Vote> votes = Collections.synchronizedMap(new HashMap<>());
        final String requestBody = new ObjectMapper().writeValueAsString(query);
        CountDownLatch countDownLatch = new CountDownLatch(replicasUrls.length);

        String urlPostfix = "/participant" + "/" + entityTypeName + "/" + operation;
        webClients.forEach((url, webClient) ->
                sendInitRequestsToReplica(url , urlPostfix, webClient, requestBody,
                        votes, countDownLatch));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("VOTES:" + votes);

        return votes;
    }

    private void sendInitRequestsToReplica(String url, String urlPostfix, WebClient webClient, String query,
                                           Map<String, Vote> voteMap, CountDownLatch countDownLatch){
        executorService.submit(() -> {
            webClient.post()
                    .uri(url + urlPostfix)
                    .body(Mono.just(query), String.class)
                    .retrieve()
                    .bodyToMono(Vote.class)
                    .doOnSuccess(vote -> {
                        voteMap.put(url, vote);
                        countDownLatch.countDown();
                    })
                    .doOnError(error -> {
                        System.err.println("ERROR : " + error.getMessage() + " :: " + error);
                        countDownLatch.countDown();
                    })
                    .blockOptional();

        });
    }


    private <E> String getCrudOperationName(Query query, Class<E> entityClass){
        String className = query.getClass().getSimpleName();
        return className.substring(0, className.indexOf(entityClass.getSimpleName()));
    }

    private boolean secondPhase(Map<String, Vote> votes){
        CanCommit canCommit = checkForAborts(votes);

        twoPhaseLogService.logAsCoordinator("Second Phase, Decision: ", "GLOBAL " + canCommit.name());

        switch (canCommit){
            case COMMIT:
                commitPhase(votes);
                return true;

            case ABORT:
                globalAbortPhase(votes);
                return false;

            default:
                return false;
        }
    }

    private CanCommit checkForAborts(Map<String, Vote> voteMap){
        Optional<Vote> optionalAbort =
                voteMap.values().stream().filter(vote -> vote.getCanCommit().equals(ABORT)).findAny();

        if(optionalAbort.isPresent()){
            return ABORT;
        }

        return COMMIT;
    }


    private void commitPhase(Map<String, Vote> votes){
        finalPhase(votes, COMMIT);
    }

    private void globalAbortPhase(Map<String, Vote> votes){
        finalPhase(votes, ABORT);
    }

    private void finalPhase(Map<String, Vote> votes, CanCommit canCommit){

        votes.forEach((url, vote) -> {
            executorService.submit(() -> {
                WebClient webClient = webClients.get(url);
                webClient.get()
                        .uri(url + "/participant/" + canCommit.name().toLowerCase() + "/" + vote.getRequestId())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .doOnSuccess(obj -> {
                            System.err.println("Committed Successfully on: " + url + obj);
                            twoPhaseLogService.logAsCoordinator("Committed Successfully on: ", url);
                        })
                        .doOnError(exception -> {
                            System.err.println("Failed to commit: " + url + ", " + exception.getMessage());
                            twoPhaseLogService.logAsCoordinator("Failed to commit: ", url + ": " + exception.getMessage());
                        }).blockOptional();
            });
        });
    }

    public void setTwoPhaseLogService(TwoPhaseLogService twoPhaseLogService) {
        this.twoPhaseLogService = twoPhaseLogService;
    }
}
