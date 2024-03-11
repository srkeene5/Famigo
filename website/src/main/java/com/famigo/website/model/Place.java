package com.famigo.website.model;

//@Component

public class Place {
    private String rating;
    private String name;
    private String address;
    private int id;
    private String description;

    // Needed for ajax
    public Place() {
    }

    public Place(String rating, String name, String address) {
        // this.jdbcTemplate = null;
        this.rating = rating;
        this.name = name;
        this.address = address;
    }

    public Place(String rating, String name, String address, int id) {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.id = id;
    }

    // Getters
    public String getRating() {
        return rating;
    }

    public int getId() {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}