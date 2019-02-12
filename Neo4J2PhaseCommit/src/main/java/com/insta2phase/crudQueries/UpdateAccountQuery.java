package com.insta2phase.crudQueries;

public class UpdateAccountQuery extends Query{
    private String email;
    private String fullName;
    private String username;
    private String oldPassword;
    private String newPassword;

    public UpdateAccountQuery() {
    }

    public UpdateAccountQuery(String email, String fullName, String username, String oldPassword, String newPassword) {
        setEmail(email);
        setFullName(fullName);
        setUsername(username);
        setOldPassword(oldPassword);
        setNewPassword(newPassword);
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UpdateAccountQuery{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
