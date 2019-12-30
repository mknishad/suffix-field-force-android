package com.suffix.fieldforce.model;

public class Chats {

    private String sender_id;
    private String sender;
    private String receiver_id;
    private String receiver;
    private String message;
    private Boolean isseen;

    public Chats(String sender_id, String sender, String receiver_id, String receiver, String message, Boolean isseen) {
        this.sender_id = sender_id;
        this.sender = sender;
        this.receiver_id = receiver_id;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public Chats() {
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsseen() {
        return isseen;
    }
}
