package com.famigo.website.model;

//@Component

public class SubPlace {
    private String rating;
    private String name;
    private String address;
    private String id;

    // Getters
    public String getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
