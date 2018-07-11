package vn.hdu.go2jp.hduchat.common;

import vn.hdu.go2jp.hduchat.R;

public enum TAB {

    CONTACT("Contacts", R.drawable.tab_ic_contacts1, R.drawable.tab_ic_contacts_selected,R.id.llContact,true),
    CHAT("Chats", R.drawable.tab_ic_chats1, R.drawable.tab_ic_chats_selected,R.id.llRoom,false),
    TIMELINE("Timeline", R.drawable.tab_ic_timeline2, R.drawable.tab_ic_timeline_selected,R.id.llTimeLine,true),
    CALL("Call", R.drawable.tab_ic_calls, R.drawable.tab_ic_calls_selected,R.id.llCall,true),
    MORE("More...", R.drawable.tab_ic_more, R.drawable.tab_ic_more_selected,R.id.llMore,true);

    private String value;
    private int resId;
    private int resIdSelected;
    private int llId;
    private boolean visible;

    TAB(String value, int resId, int resIdSelected,int llId,boolean visible) {
        this.value = value;
        this.resId = resId;
        this.resIdSelected = resIdSelected;
        this.llId = llId;
        this.visible = visible;
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

    public int getLlId() {
        return llId;
    }

    public void setLlId(int llId) {
        this.llId = llId;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
