package com.ppmdev.agriman.model;

public class MarketPlaceAdModel {


    private String productName;
    private String productDescription;
    private String productValue;
    private String profileName;

    private String userNumber;

    public MarketPlaceAdModel() {
    }

    public MarketPlaceAdModel(String productName, String productDescription, String productValue, String profileName, String userNumber) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productValue = productValue;
        this.profileName = profileName;
        this.userNumber = userNumber;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductValue() {
        return productValue;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

}
