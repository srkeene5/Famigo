package com.famigo.website.model;

//@Component

public class Place {
    private String rating;
    private String name;
    private String address;
    private String id;

    public Place(String rating, String name, String address) {
        // this.jdbcTemplate = null;
        this.rating = rating;
        this.name = name;
        this.address = address;
    }

    // Getters
    public String getRating() {
        return rating;
    }

    protected String getId() {
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