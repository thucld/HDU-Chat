package vn.hdu.go2jp.hduchat.model.data;

public class Friend extends User {
    private String avatarPath;
    private String userName;

    public Friend(){

    }

    @Override
    public String getAvatarPath() {
        return avatarPath;
    }

    @Override
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
