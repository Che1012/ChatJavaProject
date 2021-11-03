package com.db.edu.proxy;

import com.db.edu.proxy.server.user.User;
import com.db.edu.proxy.server.user.UserList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserListTest {

    private UserList newUserList;
    private Socket newSocket;
    private User newUser;

    @BeforeEach
    public void TestsPreparation() throws IOException {
        newUserList = new UserList();
        newSocket = mock(Socket.class);
        newUser = new User(newSocket);
    }
    @Test
    public void shouldReturnUserIfExists() throws IOException {

        newUserList.addUser(newUser);
        User foundUser = newUserList.findUserByName("Somebody");
        assertEquals(foundUser.getId(), "Somebody");

    }

    @Test
    public void shouldReturnNullIfUserDoNotExists() {
        User foundUser = newUserList.findUserByName("Somebody");
        assertNull(foundUser);

    }

    @Test
    public void shouldCleanUsers() throws IOException {
        newUserList.addUser(newUser);
        newUserList.clean();
        User foundUser = newUserList.findUserByName("Somebody");
        assertNull(foundUser);

    }
    @Test
    public void shouldSetId() throws IOException {
        newUserList.addUser(newUser);
        newUser.setId("NewId");
        assertEquals(newUser.getId(), "NewId");
    }

}