package com.db.edu.message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.db.edu.message.FileController;
import com.db.edu.message.StringMessage;
import static java.lang.System.lineSeparator;

public class MessageKeeper {
    private final String roomId;
    private final int SIZE_OF_BUFFER = 15;
    private final ArrayList<String> messageBuffer;
    private final FileController fileController;

    public MessageKeeper(String roomId) {
        this.roomId = roomId;
        this.fileController = new FileController(roomId);
        this.messageBuffer = fulfillBufferFromFile();
    }

    public synchronized void addMessage(StringMessage message) {
        final String messageString = message.getMessage();
        messageBuffer.add(messageString);
        removeFirstIfOverflow();
        writeMessageToFile(messageString);
    }

    public synchronized void printHistory(DataOutputStream stream) throws IOException {
        for (String message : messageBuffer) {
            stream.writeUTF(message + lineSeparator());
        }
    }

    private synchronized ArrayList<String> fulfillBufferFromFile() {
        return fileController.getLastMessages(SIZE_OF_BUFFER);
    }

    private void removeFirstIfOverflow() {
        if (messageBuffer.size() > SIZE_OF_BUFFER) {
            messageBuffer.remove(0);
        }
    }

    private void writeMessageToFile(String message) {
        fileController.addMessage(message);
    }

}
