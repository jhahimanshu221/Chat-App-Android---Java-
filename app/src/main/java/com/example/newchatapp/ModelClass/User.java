package com.example.newchatapp.ModelClass;

public class User {
    public String uid;
    public String name;
    public String email;
    public String status;

    public User() {
    }

    public User(String uid, String name, String email, String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
