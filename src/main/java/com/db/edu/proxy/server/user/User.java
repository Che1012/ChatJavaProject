package com.db.edu.proxy.server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class User {
    final Logger logger = LoggerFactory.getLogger(User.class);

    private final Socket socket;
    private String id;
    private final DataOutputStream out;
    private final DataInputStream in;

    public User(Socket socket) throws IOException {
        this.socket = socket;
        id = "Somebody";
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataOutputStream connectOut() throws IOException {
        return out;
    }

    public DataInputStream connectIn() {
        return in;
    }

    public boolean isClosed() {
        return socket.isClosed() || !socket.isConnected();
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            logger.error("User has no initialized streams or socket to close");
        }
    }

    public void flush() throws IOException {
        out.flush();
    }

    public String getReceivedLine() throws IOException {
        return connectIn().readUTF();
    }
}
