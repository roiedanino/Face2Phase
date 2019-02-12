package com.insta2phase.crudServices.account;

import com.insta2phase.crudQueries.ReadAccountQuery;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.dal.AccountDao;

import com.insta2phase.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TwoPhaseReadAccount implements TwoPhaseCrudOperation<ReadAccountQuery> {

    private AccountDao accountDao;
    private String accountEmail;

    @Autowired
    public TwoPhaseReadAccount(AccountDao accountDao) {
        setAccountDao(accountDao);
    }

    @Override
    public boolean canExecute(ReadAccountQuery request) {
        setAccountEmail(request.getEmail());
        return accountDao.existsById(accountEmail);
    }

    @Override
    public Object execute() {
        Optional<Account> optionalAccount = accountDao.findById(accountEmail, 3);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }

        throw new RuntimeException("Cannot execute before calling canExecute");
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    private void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
