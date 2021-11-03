package com.db.edu.proxy.server;

import com.db.edu.message.StringMessage;
import com.db.edu.proxy.server.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForClientThread extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ForClientThread.class);

    private final User user;
    private final List<Room> rooms;
    private Room room;

    public ForClientThread(User user, List<Room> rooms) {
        this.user = user;
        this.rooms = rooms;
        this.room = getRoomById("MainRoom", rooms);
    }

    public void run() {
        try {
            while (true) {
                workWithMessage();
                user.flush();
            }
        } catch (IOException e) {
            logger.error("Can't connect to user;s output and input");
        } finally {
            user.close();
        }
    }

    private Room getRoomById(String id, List<Room> rooms) {
        for (Room room : rooms) {
            if (id.trim().equals(room.getId().trim())) {
                return room;
            }
        }
        return room;
    }

    private void workWithMessage() throws IOException {
        String receivedLine = getReceivedLine();
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

    private String getReceivedLine() throws IOException {
        return user.getReceivedLine();
    }

    private User searchInRoomsByName(String name, List<Room> rooms) {
        for (Room room : rooms) {
            if (room.getUserList().findUserByName(name) != null) {
                return user;
            }
        }
        return null;
    }

    private void rename() throws IOException {
        String newName = getReceivedLine();
        User userWithThisName = searchInRoomsByName(newName, rooms);
        if (userWithThisName == null) {
            user.setId(newName);
        } else {
            user.connectOut().writeUTF("This name already exists!");
        }
        user.connectOut().flush();
    }

    private void sendPrivateMessage() throws IOException {
        String receiverName = getReceivedLine();
        String messageToReceive = getReceivedLine();
        User receiver = room.getUserList().findUserByName(receiverName);
        if (receiver == null) {
            user.connectOut().writeUTF("Incorrect username");
        } else {
            user.connectOut().writeUTF("Personal message for " + receiver.getId() + ": " + messageToReceive);
            receiver.connectOut().writeUTF("Personal message for you from " + user.getId() + ": " + messageToReceive);
            receiver.connectOut().flush();
        }
    }

    private void changeRoom() throws IOException {
        String newRoom = getReceivedLine();
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
