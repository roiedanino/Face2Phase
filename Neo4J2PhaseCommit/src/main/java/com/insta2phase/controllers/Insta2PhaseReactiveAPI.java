package com.insta2phase.controllers;

import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import com.insta2phase.entities.TwoPhaseLog;
import com.insta2phase.services.log.TwoPhaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin
public class Insta2PhaseReactiveAPI {
    private AccountDao accountDao;
    private TwoPhaseLogService twoPhaseLogService;

    @Autowired
    public Insta2PhaseReactiveAPI(AccountDao accountDao, TwoPhaseLogService twoPhaseLogService){
        setAccountDao(accountDao);
        setTwoPhaseLogService(twoPhaseLogService);
    }

    public Mono<Account> readAccount(){
        return Mono.create((emitter) -> {
            emitter.success(new Account("roiedanino@gmail.com", "roiedanino", "roie", "danino"));
        });
    }

    @RequestMapping(
            path="/accounts/all",
            method= RequestMethod.GET,
            produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Account> readAccounts(@RequestParam(name="size", required=false, defaultValue="10") int size,
                                               @RequestParam(name="page", required=false, defaultValue="0") int page){

        Iterator<Account> accountIterator = accountDao.findAll().iterator();
        return Flux.create(emitter -> {
            while(accountIterator.hasNext()) {
                emitter.next(accountIterator.next());
            }
            emitter.complete();
        });

    }

    @RequestMapping(
            path="/accounts/list",
            method= RequestMethod.GET,
            produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Account>> readAccountsList(@RequestParam(name="size", required=false, defaultValue="10") int size,
                                                @RequestParam(name="page", required=false, defaultValue="0") int page){

        Iterable<Account> accountIterator = accountDao.findAll();
        List<Account> accountList = new ArrayList<>();
        accountIterator.forEach(accountList::add);
        return Flux.create(emitter -> {
            emitter.next(accountList);
            emitter.complete();
        });

    }

    @RequestMapping(
            path="/logs/all",
            method= RequestMethod.GET,
            produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TwoPhaseLog> readLogs(){

        return twoPhaseLogService.getAllLogsFlux();

    }

//    @RequestMapping(
//            path="/flux",
//            method= RequestMethod.GET,
//            produces= MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<Object> readAccounts(){
        Random random = new Random();
        return Flux.defer( () -> Flux.create(emitter -> {
            for (int i = 0; i < 10; i++) {
                Account account = new Account("email" + random.nextInt(100), "username" + random.nextInt(100),
                        "firstName" + random.nextInt(100), "lastname" + random.nextInt(100));
                emitter.next(account);
            }
            emitter.complete();
        }));

    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setTwoPhaseLogService(TwoPhaseLogService twoPhaseLogService) {
        this.twoPhaseLogService = twoPhaseLogService;
    }
}
