package vn.hdu.go2jp.hduchat.model.data;

import java.util.Date;

import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;

public class Message {
    private String userId;
    private String message;
    private boolean visible;
    private Date time;
    private UserType userType;
    private Status status;

    public Message(String message, Date time, UserType userType, Status status) {
        this.message = message;
        this.time = time;
        this.userType = userType;
        this.status = status;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
