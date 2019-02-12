package com.insta2phase.crudQueries;

public class CreateAccountQuery extends Query{
    private String email;
    private String fullName;
    private String username;
    private String password;


    public CreateAccountQuery() { }

    public CreateAccountQuery(String email, String fullName, String username, String password) {
        setEmail(email);
        setFullName(fullName);
        setUsername(username);
        setPassword(password);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CreateAccountQuery{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
