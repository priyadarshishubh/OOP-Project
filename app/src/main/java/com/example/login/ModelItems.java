package com.example.login;

public class ModelItems {
    private String itemID, itemName, itemDescription, itemQuantity,
            itemCategory, itemImage, timestamp,uid;

    public ModelItems(){

    }
    public ModelItems(String itemID, String itemCategory, String itemDescription, String itemImage,
                      String itemName, String itemQuantity, String timestamp, String uid){
        this.itemID = itemID;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemImage = itemImage;
        this.timestamp = timestamp;
        this.uid = uid;

    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
