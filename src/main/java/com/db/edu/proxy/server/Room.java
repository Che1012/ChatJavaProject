package com.db.edu.proxy.server;

import com.db.edu.message.MessageKeeper;
import com.db.edu.message.StringMessage;
import com.db.edu.proxy.server.user.User;
import com.db.edu.proxy.server.user.UserList;

import java.io.DataOutputStream;
import java.io.IOException;

public class Room {
    private final String roomId;
    private final UserList users;
    private final MessageKeeper keeper;

    public Room(String roomId) {
        this.roomId = roomId;
        this.users = new UserList();
        this.keeper = new MessageKeeper(roomId);
    }

    public String getId() {
        return roomId;
    }

    public void addUser(User user) {
        users.addUser(user);
    }

    public void removeUser(User user) {
        users.removeUser(user);
    }

    public UserList getUserList() {
        return users;
    }

    public void clean() {
        users.clean();
    }

    public void sendToEveryone(String message) {
        users.sendToEveryone(message);
    }

    public void addMessage(StringMessage message) {
        keeper.addMessage(message);
    }

    public void printHistory(DataOutputStream out) throws IOException {
        keeper.printHistory(out);
    }
}
