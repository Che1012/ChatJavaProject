package com.db.edu.proxy;

import com.db.edu.parser.MessageParser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Disabled
    @Test
    public void baseParserTest() {
        MessageParser parser = new MessageParser();
        String clientMessage = "/snd hello";
        clientMessage = parser.parse(clientMessage);

        assertEquals(" hello", clientMessage);
    }
}
