package com.insta2phase.crudServices.account;

import com.insta2phase.crudQueries.DeleteAccountQuery;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TwoPhaseDeleteAccount implements TwoPhaseCrudOperation<DeleteAccountQuery> {
    private DeleteAccountQuery query;
    private AccountDao accountDao;

    @Autowired
    public TwoPhaseDeleteAccount(AccountDao accountDao) {
        setAccountDao(accountDao);
    }


    @Override
    public boolean canExecute(DeleteAccountQuery request) {
        if(accountDao.existsById(request.getEmail())) {
            setQuery(request);
            return true;
        }
        return false;
    }

    @Override
    public Object execute() {
        Optional<Account> optionalAccount = accountDao.findById(query.getEmail(), 2);
        if(optionalAccount.isPresent()) {
            accountDao.deleteById(query.getEmail());
            return optionalAccount.get();
        }else {
            throw new RuntimeException("Cannot update account: " + query.getEmail() + " was not found, " +
                    "should call canExecute() first");
        }
    }


    private void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    private void setQuery(DeleteAccountQuery query) {
        this.query = query;
    }
}
