package com.db.edu.proxy.server.user;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.db.edu.proxy.server.user.User;
import static java.lang.System.lineSeparator;

public class UserList {
    private final ArrayList<User> users;

    public UserList() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void sendToEveryone(String message) {
        for (User user : users) {
            try {
                DataOutputStream out = user.connectOut();
                out.writeUTF(lineSeparator() + message);
                out.flush();

            } catch (IOException e) {
                System.out.println("Error:" + e);
            }
        }
    }

    public User findUserByName(String name) {
        for (User user : users) {
            if (name.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    public void clean() {
        users.removeIf(User::isClosed);
    }
}
