package com.famigo.website.model;

public class SubFollow {
    private String userDoingFollowing;
    private String userToBeFollowed;
    private int followerCount;
    private boolean isFollowing;

    public String getUserDoingFollowing() {
        return userDoingFollowing;
    }

    public String getUserToBeFollowed() {
        return userToBeFollowed;
    }

    public int getFollowerCount() {
        return followerCount;
    }
    public boolean getIsFollowing() {
        return isFollowing;
    }

    public void setUserDoingFollowing(String userDoingFollowing) {
        this.userDoingFollowing = userDoingFollowing;
    }

    public void setUserToBeFollowed(String userToBeFollowed) {
        this.userToBeFollowed = userToBeFollowed;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}
