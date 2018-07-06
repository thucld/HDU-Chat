package vn.hdu.go2jp.hduchat.common;

public enum Tag {

    CONTACT("Contact"),
    CHAT("Chat"),
    TIMELINE("Timeline"),
    CALL("Call"),
    MORE("More...");

    private String value;

    Tag(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
