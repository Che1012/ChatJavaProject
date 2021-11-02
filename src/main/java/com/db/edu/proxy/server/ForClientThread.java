package com.db.edu.proxy.server;

import com.db.edu.message.StringMessage;
import com.db.edu.proxy.server.user.User;

import java.io.IOException;
import java.util.ArrayList;

public class ForClientThread extends Thread {
    private final User user;
    private final ArrayList<Room> rooms;
    private Room room;

    public ForClientThread(User user, ArrayList<Room> rooms) {
        this.user = user;
        this.rooms = rooms;
        this.room = getRoomById("MainRoom", rooms);
    }

    private Room getRoomById(String id, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            if (id.trim().equals(room.getId().trim())) {
                return room;
            }
        }
        return room;
    }

    public void run() {
        try {
            while (true) {
                workWithMessage();
                user.connectOut().flush();
            }

        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }

    private void workWithMessage() throws IOException {
        String receivedLine = user.connectIn().readUTF();
        switch (receivedLine) {
            case "/hist":
                room.printHistory(user.connectOut());
                break;
            case "/chid":
                rename();
                break;
            case "/sdnp":
                sendPrivateMessage();
                break;
            case "/chroom":
                changeRoom();
                break;
            default:
                sendMessage(receivedLine);
                break;
        }
    }

    private User searchInRoomsByName(String name, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            if (room.getUserList().findUserByName(name) != null) {
                return user;
            }
        }
        return null;
    }

    private void rename() throws IOException {
        String newName = user.connectIn().readUTF();
        User userWithThisName = searchInRoomsByName(newName, rooms);
        if (userWithThisName == null) {
            user.setId(newName);
        } else {
            user.connectOut().writeUTF("This name already exists!");
        }
        user.connectOut().flush();
    }

    private void sendPrivateMessage() throws IOException {
        String receiverName = user.connectIn().readUTF();
        String messageToReceive = user.connectIn().readUTF();
        User receiver = room.getUserList().findUserByName(receiverName);
        if (receiver == null) {
            user.connectOut().writeUTF("Incorrect username");
        } else {
            user.connectOut().writeUTF("Personal message for " + receiver.getId() + ": " + messageToReceive);
            receiver.connectOut().writeUTF("Personal message for you from " + user.getId() + ": " + messageToReceive);
        }
    }

    private void changeRoom() throws IOException {
        String newRoom = user.connectIn().readUTF();
        room.removeUser(user);
        room = getRoomById(newRoom, rooms);
        room.addUser(user);
        user.connectOut().writeUTF("You changed room to " + room.getId());
    }

    private void sendMessage(String receivedLine) throws IOException {
        StringMessage message = new StringMessage(receivedLine, user);
        room.addMessage(message);
        room.sendToEveryone(message.getMessage());
    }
}
