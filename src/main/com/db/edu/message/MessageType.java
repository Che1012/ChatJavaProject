package main.com.db.edu.message;

public enum MessageType {
    SEND("/snd"),
    HIST("/hist"),
    CHROOM("/chroom"),
    CHID("/chid");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
