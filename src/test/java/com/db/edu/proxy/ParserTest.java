package com.db.edu.proxy;

import com.db.edu.parser.MessageParser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.fest.assertions.Assertions.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    MessageParser parser =  new MessageParser();
    String clientMessage = "";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream stream = new PrintStream(outContent);

    @Test
    public void baseMessageSent() {
        clientMessage = "/snd hello";
        clientMessage = parser.parse(clientMessage);
        assertEquals("hello", clientMessage);
    }

    @Test
    public void messageSizeOverMaxLength() {
        clientMessage =
                "/snd 12345_12345_12345_12345_12345_" +
                        "12345_12345_12345_12345_12345_" +
                        "12345_12345_12345_12345_12345_" +
                        "12345_12345_12345_12345_12345_" +
                        "12345_12345_12345_12345_12345_!";
//        clientMessage = parser.parse(clientMessage);
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,()->parser.parse(clientMessage));
        assertThat(thrown).hasMessage("Sorry, but your message is too long," +
                " please, use no more than 150 symbols\n");


    }

    @Test
    public void promiscuousMessageSent() {
        clientMessage = "/snd Hello Мир!№;(%?:%№Ё";
        clientMessage = parser.parse(clientMessage);

        assertEquals("Hello Мир!№;(%?:%№Ё", clientMessage);
    }


    @Test
    public void noCommandBeforeMessageSent() {
        clientMessage = "Hello";
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,()->parser.parse(clientMessage));
        assertThat(thrown).hasMessage("You sent a message without any command. Please try again with existing commands. Ex: /snd message\n");
    }




    @Disabled
    @Test
    public void baseParserTest() {
        MessageParser parser = new MessageParser();
        String clientMessage = "/snd hello";
        clientMessage = parser.parse(clientMessage);

        assertEquals(" hello", clientMessage);
    }
}
