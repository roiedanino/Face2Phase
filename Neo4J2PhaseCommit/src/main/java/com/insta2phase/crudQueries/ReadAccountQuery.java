package com.insta2phase.crudQueries;

public class ReadAccountQuery extends Query{
    private String email;

    public ReadAccountQuery(String email) {
        this.email = email;
    }

    public ReadAccountQuery(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ReadAccountQuery{" +
                "email='" + email + '\'' +
                '}';
    }
}
