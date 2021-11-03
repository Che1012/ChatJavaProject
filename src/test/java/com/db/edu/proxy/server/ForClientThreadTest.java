package com.db.edu.proxy.server;

import com.db.edu.proxy.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ForClientThreadTest {

    private User user;
    private Room room;
    private ForClientThread forClientThreadSut;

    @BeforeEach
    public void setUp() throws IOException {
        user = mock(User.class);
        room = mock(Room.class);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);

        when(room.getId()).thenReturn("MainRoom");
        doThrow(new IOException()).when(user).flushOut();

        forClientThreadSut = new ForClientThread(user, rooms);
    }

    @Test
    public void shouldSendMessageWhenReceiveStringMessage() throws IOException {
        when(user.getReceivedLine()).thenReturn("message");

        forClientThreadSut.run();

        verify(room).sendToEveryone(any());
    }
}
