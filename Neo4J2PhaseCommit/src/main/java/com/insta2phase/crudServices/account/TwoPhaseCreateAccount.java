package com.insta2phase.crudServices.account;

import com.insta2phase.crudQueries.CreateAccountQuery;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class TwoPhaseCreateAccount implements TwoPhaseCrudOperation<CreateAccountQuery> {
    private AccountDao accountDao;
    private CreateAccountQuery query;

    @Autowired
    public TwoPhaseCreateAccount(AccountDao accountDao) {
        setAccountDao(accountDao);
    }

    @Override
    public boolean canExecute(CreateAccountQuery request) {

        if(accountDao.existsById(request.getEmail())){
           return false;
        }

        setQuery(request);
        return true;
    }

    @Override
    public Account execute() {
        if(query == null){
            throw new RuntimeException("Cannot execute before calling canExecute");
        }

        Account account = new Account(query);
        String passwordHash = DigestUtils.md5DigestAsHex(query.getPassword().getBytes());
        account.setPasswordHash(passwordHash);
        return accountDao.save(account);
    }


    private void setQuery(CreateAccountQuery query) {
        this.query = query;
    }

    private void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
