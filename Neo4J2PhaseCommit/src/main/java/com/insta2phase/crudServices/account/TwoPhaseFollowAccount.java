package com.insta2phase.crudServices.account;

import com.insta2phase.crudQueries.FollowAccountQuery;
import com.insta2phase.crudServices.TwoPhaseCrudOperation;
import com.insta2phase.dal.AccountDao;
import com.insta2phase.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TwoPhaseFollowAccount implements TwoPhaseCrudOperation<FollowAccountQuery> {

    private FollowAccountQuery query;
    private AccountDao accountDao;

    private Account followed;
    private Account follower;

    @Autowired
    public TwoPhaseFollowAccount(AccountDao accountDao) {
        setAccountDao(accountDao);
    }

    @Override
    public boolean canExecute(FollowAccountQuery request) {
        Optional<Account> optionalFollowed = accountDao.findById(request.getFollowed().getEmail(), 3);
        Optional<Account> optionalFollower = accountDao.findById(request.getFollower().getEmail(), 3);

        if(optionalFollowed.isPresent() && optionalFollower.isPresent()){
            followed = optionalFollowed.get();
            follower = optionalFollower.get();

            if(!follower.isFollowing(followed)) {
                setQuery(request);
                return true;
            }
        }

        return false;
    }

    @Override
    public Object execute() {
        if(query != null && followed != null && follower != null && !follower.isFollowing(followed)){
            follower.follow(followed);

            return accountDao.save(follower);
        }

        throw new RuntimeException("Could not commit follow account: " + query + ", call canExecute() before calling execute()");
    }

    public FollowAccountQuery getQuery() {
        return query;
    }

    public void setQuery(FollowAccountQuery query) {
        this.query = query;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
