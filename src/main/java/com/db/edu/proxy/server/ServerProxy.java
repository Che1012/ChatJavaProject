package com.db.edu.proxy.server;

import com.db.edu.SocketHolder;
import com.db.edu.proxy.server.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.db.edu.proxy.server.Room;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerProxy {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ServerProxy.class);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("MainRoom"));
        rooms.add(new Room("OtherRoom"));

        try (ServerSocket serverSocket = new ServerSocket(SocketHolder.getPort())) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    if (socket != null) {
                        logger.info("Caught user");
                        User user = new User(socket);
                        rooms.get(0).addUser(user);
                        new ForClientThread(user, rooms).start();
                    }
                    cleanRooms(rooms);
                } catch (IOException e) {
                    logger.error("No users");
                }
            }
        } catch (Exception e) {
            logger.error("Something went wrong " + e);
        }
    }

    private static void cleanRooms(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            room.clean();
        }
    }
}
