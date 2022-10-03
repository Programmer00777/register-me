package com.chernikov.registerme.email;

public interface EmailSender {
    void send(String to, String text);
}
