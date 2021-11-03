package com.db.edu.parser;

import com.db.edu.message.MessageType;

import java.util.Objects;

public class MessageParser {
    public String parse(String message) {
        final int maxLength = 150;
        if (message.startsWith(MessageType.SEND.getType())) {
            if (message.trim().equals(MessageType.SEND.getType())) {
                throw new IllegalArgumentException("Please, input message\n");
            } else if (message.length() > maxLength) {
                throw new IllegalArgumentException("Sorry, but your message is too long," +
                        " please, use no more than 150 symbols\n");
            } else {
                return message.substring(MessageType.SEND.getType().length() + 1);
            }
        } else if (Objects.equals(MessageType.HIST.getType(), message)) {
            return message;
        } else if (message.startsWith(MessageType.CHROOM.getType())) {
            if (message.trim().equals(MessageType.CHROOM.getType())) {
                throw new IllegalArgumentException("Please, input room name\n");
            }
            return message;
        } else if (message.startsWith(MessageType.CHID.getType())) {
            if (message.split(" ").length < 2) {
                throw new IllegalArgumentException("Please, input your name\n");
            }
            return message;
        } else if (message.startsWith(MessageType.SDNP.getType())) {
            if (message.split(" ").length < 3) {
                throw new IllegalArgumentException("Please, input receiver name and message\n");
            }
            return message;
        } else {
            throw new IllegalArgumentException("You sent a message without any command." +
                    " Please try again with existing commands. Ex: /snd message\n");
        }
    }
}
