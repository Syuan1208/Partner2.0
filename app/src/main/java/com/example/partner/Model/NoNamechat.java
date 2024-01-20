package com.example.partner.Model;

public class NoNamechat {
    private String message;
    private String username;
    private String status;

    public NoNamechat(String message, String username, String status) {
        this.message = message;
        this.username = username;
        this.status = status;
    }

    public NoNamechat() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
