package vn.hdu.go2jp.hduchat.data.models;

import java.util.List;

public class User {
    private String userId;
    private String passWord;
    private String userName;
    private List<String> listRoomId;
    private String phoneNumber;
    private String email;
    private List<String> contactId;
    private String avatarPath;
    private String note;

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

    public List<String> getListRoomId() {
        return listRoomId;
    }

    public void setListRoomId(List<String> listRoomId) {
        this.listRoomId = listRoomId;
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

    public List<String> getContactId() {
        return contactId;
    }

    public void setContactId(List<String> contactId) {
        this.contactId = contactId;
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



}
