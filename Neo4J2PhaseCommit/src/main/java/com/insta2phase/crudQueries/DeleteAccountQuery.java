package com.insta2phase.crudQueries;


public class DeleteAccountQuery extends Query{
    private String email;

    public DeleteAccountQuery() {
    }

    public DeleteAccountQuery(String email) {
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DeleteAccountQuery{" +
                "email='" + email + '\'' +
                '}';
    }
}
