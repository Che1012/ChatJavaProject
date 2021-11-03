package com.db.edu.message;

import com.db.edu.proxy.server.user.User;

import java.time.LocalTime;

public class StringMessage {
    private final String value;
    private final LocalTime time;
    private final User user;

    public StringMessage(String value, User user) {
        this.value = value;
        time = LocalTime.now();
        this.user = user;
    }

    public String getMessage() {
        return (user.getId() + ": " + value + " (" + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + ")");
    }
}
