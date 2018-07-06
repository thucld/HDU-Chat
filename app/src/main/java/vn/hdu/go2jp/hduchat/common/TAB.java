package vn.hdu.go2jp.hduchat.common;

import vn.hdu.go2jp.hduchat.R;

public enum TAB {

    CONTACT("Contact", R.drawable.tab_ic_contacts1, R.drawable.tab_ic_contacts_selected),
    CHAT("Chat", R.drawable.tab_ic_chats1, R.drawable.tab_ic_chats_selected),
    TIMELINE("Timeline", R.drawable.tab_ic_timeline2, R.drawable.tab_ic_timeline_selected),
    CALL("Call", R.drawable.tab_ic_calls, R.drawable.tab_ic_calls_selected),
    MORE("More...", R.drawable.tab_ic_more, R.drawable.tab_ic_more_selected);

    private String value;
    private int resId;
    private int resIdSelected;

    TAB(String value, int resId, int resIdSelected) {
        this.value = value;
        this.resId = resId;
        this.resIdSelected = resIdSelected;
    }

    public String getValue() {
        return value;
    }

    public int getIcon() {
        return resId;
    }

    public int getIconSelected() {
        return resIdSelected;
    }
}
