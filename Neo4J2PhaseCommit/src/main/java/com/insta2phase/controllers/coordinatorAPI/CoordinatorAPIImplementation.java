package com.insta2phase.controllers.coordinatorAPI;

import com.insta2phase.crudQueries.*;
import com.insta2phase.entities.Account;
import com.insta2phase.services.coordinator.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping(path="/primary/accounts")
public class CoordinatorAPIImplementation implements CoordinatorAPI {
    private CoordinatorService coordinatorService;

    @Autowired
    public CoordinatorAPIImplementation(CoordinatorService coordinatorService){
        setCoordinatorService(coordinatorService);
    }

    @PostMapping
    @Override
    public Mono<Account> createAccount(@RequestBody CreateAccountQuery createAccountQuery) {
        return Mono.create(emitter -> {
            Optional<Account> optionalAccount = coordinatorService.executeQuery(createAccountQuery, Account.class);
            optionalAccount.ifPresent(emitter::success);
        });
    }

    @GetMapping(path = "/{email}")
    @Override
    public Mono<Account> readAccount(@PathVariable("email") String accountEmail) {
        return Mono.create(emitter -> {
            Optional<Account> optionalAccount = coordinatorService.executeQuery(new ReadAccountQuery(accountEmail), Account.class);
            optionalAccount.ifPresent(emitter::success);
        });
    }

    @PutMapping
    @Override
    public void updateAccount(@RequestBody UpdateAccountQuery updateAccountQuery) {
        System.err.println("UPDATE ACCOUNT");
        coordinatorService.executeQuery(updateAccountQuery, Account.class);
    }

    @DeleteMapping(path = "/{email}")
    @Override
    public void deleteAccount(@PathVariable("email") String email) {
        coordinatorService.executeQuery(new DeleteAccountQuery(email), Account.class);
    }

    @PostMapping(path = "/follow")
    @Override
    public Mono<Account> follow(@RequestBody FollowAccountQuery followQuery) {
        return Mono.create(emitter ->{
            Optional<Account> optionalAccount = coordinatorService.executeQuery(followQuery, Account.class);
            optionalAccount.ifPresent(emitter::success);
        });
    }

    private void setCoordinatorService(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }
}
