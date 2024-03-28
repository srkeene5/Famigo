package com.famigo.website.model;
import java.util.Objects;

public class OwnerRequest {
    private int ownerReqID;
    private String userId;
    private int placeId;
    private String placeName;

    public OwnerRequest(String userId, int placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }
    public OwnerRequest(int ownerReqID, String userId, int placeId) {
        this.ownerReqID = ownerReqID;
        this.userId = userId;
        this.placeId = placeId;
    }

    public OwnerRequest(int ownerReqID, String userId, String placeName) {
        this.ownerReqID = ownerReqID;
        this.userId = userId;
        this.placeName = placeName;
    }
    
    public int getOwnerReqID() {
        return ownerReqID;
    }
    
    public String getUserId() {
        return userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }
    
}
