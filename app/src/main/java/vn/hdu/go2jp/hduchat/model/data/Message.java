package vn.hdu.go2jp.hduchat.model.data;

import com.google.firebase.database.ServerValue;

import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;

public class Message {
    private String userId;
    private String message;
    private boolean visible;
    private Object timestamp;
    private UserType userType;
    private Status status;

    public Message(){}

    public Message(String userId, String message, UserType userType, Status status) {
        this.userId = userId;
        this.message = message;
        this.userType = userType;
        this.status = status;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
