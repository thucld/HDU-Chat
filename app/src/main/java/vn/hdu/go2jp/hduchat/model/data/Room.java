package vn.hdu.go2jp.hduchat.model.data;


import java.util.HashMap;

public class Room {
    private String roomId;
    private String title;
    private Message lastMessage;
    private HashMap<String, Boolean> contacts;

    public Room() {
    }

    public Room(String roomId, String title, HashMap<String, Boolean> contacts) {

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

    public HashMap<String, Boolean> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, Boolean> contacts) {
        this.contacts = contacts;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}

