package com.famigo.website;

public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Visibility visibility;
    private String name;
    private String description;
    
    public User(String id, String username, String email, String password, String name, String description, Visibility visibility, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
    }
    
    public String getID() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Visibility getVisibility() {
        return visibility;
    }
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
