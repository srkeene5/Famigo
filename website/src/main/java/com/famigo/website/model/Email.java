package com.famigo.website.model;

public class Email {
    private String userEmail;  
    private String userName;  
    private boolean verified;

    public Email(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.verified = false;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getVerified() {
        return verified;
    }

    public static void main(String[] args) {
        Email email = new Email("email", "name");
        email.getVerified();
    }
}
