package com.figure.anothertest.bottomvavigation;

public class BottomItem {
    private int itemId;
    private int itemIconId;
    private String itemName;
    private boolean hasNotification;

    public BottomItem(int itemId, int itemIconId, String itemName, boolean hasNotification) {
        this.itemId = itemId;
        this.itemIconId = itemIconId;
        this.itemName = itemName;
        this.hasNotification = hasNotification;
    }

    public BottomItem(int itemId, int itemIconId, boolean hasNotification) {
        this.itemId = itemId;
        this.itemIconId = itemIconId;
        this.hasNotification = hasNotification;
    }

    public boolean isHasNotification() {
        return hasNotification;
    }

    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemIconId() {
        return itemIconId;
    }

    public void setItemIconId(int itemIconId) {
        this.itemIconId = itemIconId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
