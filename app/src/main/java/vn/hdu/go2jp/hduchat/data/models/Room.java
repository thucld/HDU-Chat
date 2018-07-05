package vn.hdu.go2jp.hduchat.data.models;

import java.util.HashMap;

public class Room {
    private String roomId;
    private String title;
    private String lastMessage;
    private HashMap<String, Message> messages;
    private HashMap<String, String> contacts;

    public Room() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public HashMap<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, Message> messages) {
        this.messages = messages;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }
}
