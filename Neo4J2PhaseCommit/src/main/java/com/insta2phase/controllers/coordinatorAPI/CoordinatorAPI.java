package com.insta2phase.controllers.coordinatorAPI;

import com.insta2phase.crudQueries.*;
import com.insta2phase.entities.Account;
import com.insta2phase.utils.log.PrimaryLog;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface CoordinatorAPI {
    @PrimaryLog
    Mono<Account> createAccount(CreateAccountQuery createAccountQuery);

    @PrimaryLog
    Mono<Account> readAccount(@PathVariable("email") String accountEmail);

    @PrimaryLog
    void updateAccount(UpdateAccountQuery updateAccountQuery);

    @PrimaryLog
    void deleteAccount(@PathVariable("email") String email);

    @PrimaryLog
    Mono<Account> follow(FollowAccountQuery followQuery);
}
