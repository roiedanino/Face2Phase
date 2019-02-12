package com.insta2phase.entities;

import com.insta2phase.crudQueries.CreateAccountQuery;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NodeEntity("Account")
public class Account {
    @Id
    private String email;
    private String fullName;
    private String username;
    private String passwordHash;

    @Relationship("FOLLOWING")
    private Set<Account> following;

    public Account() {
        following = new HashSet<>();
    }

    public Account(String email, String fullName, String username, String passwordHash) {
        this();
        setEmail(email);
        setFullName(fullName);
        setUsername(username);
        setPasswordHash(passwordHash);
    }

    public Account(CreateAccountQuery createAccountQuery){
        this(createAccountQuery.getEmail(), createAccountQuery.getFullName(), createAccountQuery.getUsername(), createAccountQuery.getPassword());
        setPasswordHash("");
    }

    public boolean isFollowing(Account other){
        return following.contains(other);
    }

    public void follow(Account other){
        following.add(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(email, account.email) &&
                Objects.equals(fullName, account.fullName) &&
                Objects.equals(username, account.username) &&
                Objects.equals(passwordHash, account.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, fullName, username, passwordHash);
    }

    public Set<Account> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Account> following) {
        this.following = following;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Account{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", following=" + following +
                '}';
    }
}
