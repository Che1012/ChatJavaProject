package com.db.edu.proxy.server.user;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.System.lineSeparator;

public class UserList {

    private final List<User> users;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public UserList() {
        this.users = new LinkedList<>();
    }

    public void addUser(User user) {
        lock.writeLock().lock();
        try {
            users.add(user);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeUser(User user) {
        lock.writeLock().lock();
        try {
            users.remove(user);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void sendToEveryone(String message) {
        lock.readLock().lock();
        try {
            for (User user : users) {
                try {
                    DataOutputStream out = user.connectOut();
                    out.writeUTF(lineSeparator() + message);
                    out.flush();

                } catch (IOException e) {
                    System.out.println("Error:" + e);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public User findUserByName(String name) {
        lock.readLock().lock();
        try {
            for (User user : users) {
                if (name.equals(user.getId())) {
                    return user;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    public void clean() {
        lock.writeLock().lock();
        try {
            users.removeIf(User::isClosed);
        } finally {
            lock.writeLock().unlock();
        }
    }
}