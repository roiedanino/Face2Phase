package com.insta2phase.crudQueries;

import com.insta2phase.entities.Account;

public class FollowAccountQuery extends Query{
    private Account follower;
    private Account followed;

    public FollowAccountQuery(){

    }

    public FollowAccountQuery(Account follower, Account followed) {
        setFollower(follower);
        setFollowed(followed);
    }

    public Account getFollower() {
        return follower;
    }

    public void setFollower(Account follower) {
        this.follower = follower;
    }

    public Account getFollowed() {
        return followed;
    }

    public void setFollowed(Account followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "FollowAccountQuery{" +
                "follower=" + follower +
                ", followed=" + followed +
                '}';
    }
}
