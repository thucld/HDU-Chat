package vn.hdu.go2jp.hduchat.model.data;

import java.util.HashMap;
import java.util.List;

public class User {
    private String userId;
    private String passWord;
    private String userName;
    private HashMap<String, String> roomsId;
    private String phoneNumber;
    private String email;
    private HashMap<String, String> contacts;
    private String avatarPath;
    private String note;

    public User() {
    }

    public User(String uId, String passWord, String email){
        this.userId = uId;
        this.passWord = passWord;
        this.email = email;
    }

    public User(String userId, String passWord, String userName, HashMap<String, String> roomsId, String phoneNumber, String email, HashMap<String, String> contacts, String avatarPath, String note) {
        this.userId = userId;
        this.passWord = passWord;
        this.userName = userName;
        this.roomsId = roomsId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.contacts = contacts;
        this.avatarPath = avatarPath;
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }

    public HashMap<String, String> getRoomsId() {
        return roomsId;
    }

    public void setRoomsId(HashMap<String, String> roomsId) {
        this.roomsId = roomsId;
    }
}
