package com.insta2phase.crudServices.account;

import com.insta2phase.crudQueries.UpdateAccountQuery;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Optional;

@Component
public class TwoPhaseUpdateAccount implements TwoPhaseCrudOperation<UpdateAccountQuery> {
    private UpdateAccountQuery query = null;
    private AccountDao accountDao;

    @Autowired
    public TwoPhaseUpdateAccount(AccountDao accountDao) {
        setAccountDao(accountDao);
    }

    @Override
    public boolean canExecute(UpdateAccountQuery request) {
        Optional<Account> optionalAccount = accountDao.findById(request.getEmail());
        if(optionalAccount.isPresent()) {
            Account originalAccount = optionalAccount.get();
            String passwordHashValidation = DigestUtils.md5DigestAsHex(request.getOldPassword().getBytes());
            if(originalAccount.getPasswordHash().equals(passwordHashValidation)) {
                setQuery(request);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object execute() {
        if(query != null) {
            String newPasswordHash = DigestUtils.md5DigestAsHex(query.getNewPassword().getBytes());
            return accountDao.save(new Account(query.getEmail(), query.getFullName(), query.getUsername(), newPasswordHash));
        }

        throw new RuntimeException("Cannot update account: " + query.getEmail() +" was not found, " +
                "should call canExecute() first");
    }

    private void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    private void setQuery(UpdateAccountQuery query) {
        this.query = query;
    }
}
