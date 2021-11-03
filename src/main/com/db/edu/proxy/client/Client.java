package main.com.db.edu.proxy.client;


import main.com.db.edu.SocketHolder;
import main.com.db.edu.message.MessageType;
import main.com.db.edu.parser.MessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Client {

    private final Logger logger = LoggerFactory.getLogger(Client.class);
    private final BufferedReader reader;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public Client() throws UnsupportedEncodingException {
        this.reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }

    public Client(BufferedReader reader) {
        this.reader = reader;
    }

    public void run() {
        try (
                final Socket socket = new Socket(SocketHolder.getAddress(), SocketHolder.getPort());
                final DataInputStream input = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                final DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()));
        ) {
            MessageParser parser = new MessageParser();
            socket.setSoTimeout(3000);

            while (true) {
                readClientMessage(reader, out, parser);
                getInfoFromServer(input);
//                checkAvailability(out, input);
            }

        } catch (IOException e) {
            System.out.println("Oops, something went wrong, please, see the logs\n");
            logger.error("Error occurred in client ", e);
        }
    }

    private void checkAvailability(DataOutputStream out, DataInputStream input) throws IOException {
        out.writeUTF(MessageType.CHECK.getType());
        out.flush();
        if ((input.available() > 0 && !Objects.equals(input.readUTF(), MessageType.CHECK.getType()))
                || input.available() < 0) {
            System.out.println("Sorry, server is not available\n");
            throw new IllegalArgumentException("Cannot connect to server");
        }
    }

    private void getInfoFromServer(DataInputStream input) throws IOException {
        if (input.available() > 0) {
            String messageFromServer = input.readUTF();
            PrintStream ps = new PrintStream(System.out, true, "UTF-8");
            ps.println(messageFromServer);
        }
    }

    private void readClientMessage(BufferedReader reader, DataOutputStream out, MessageParser parser)
            throws IOException {
        String clientMessage = "";
        if (reader.ready()) {
            clientMessage = reader.readLine();
        }
        if (clientMessage != null && !clientMessage.isEmpty()) {
            try {
                clientMessage = parser.parse(clientMessage);
                clientMessage = sendMessageSeparately(out, clientMessage);
                out.writeUTF(clientMessage);
                out.flush();
            } catch (IllegalArgumentException e) {
                logger.error("Client input is incorrect: " + clientMessage);
                System.out.println(e.getMessage());
            }
        }
    }

    private String sendMessageSeparately(DataOutputStream out, String clientMessage) throws IOException {
        if (clientMessage.startsWith(MessageType.CHID.getType())) {
            out.writeUTF(MessageType.CHID.getType());
            out.flush();
            clientMessage = clientMessage.substring(MessageType.CHID.getType().length()).trim();
            setName(clientMessage);
        } else if (clientMessage.startsWith(MessageType.CHROOM.getType())) {
            out.writeUTF(MessageType.CHROOM.getType());
            out.flush();
            clientMessage = clientMessage.substring(MessageType.CHROOM.getType().length());
            setName(clientMessage);
        } else if (clientMessage.startsWith(MessageType.SDNP.getType())) {
            String[] receiverName = clientMessage.split(" ");
            if (receiverName.length < 3) {
                throw new IllegalArgumentException("Input message\n");
            }
            out.writeUTF(MessageType.SDNP.getType());
            out.flush();
            out.writeUTF(receiverName[1]);
            out.flush();
            clientMessage = clientMessage.substring(MessageType.SDNP.getType().length() + receiverName[1].length() + 2);
        }
        return clientMessage;
    }
}
