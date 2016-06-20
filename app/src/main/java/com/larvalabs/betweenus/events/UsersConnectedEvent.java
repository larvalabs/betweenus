package com.larvalabs.betweenus.events;

public class UsersConnectedEvent {

    private String otherUsername;

    public UsersConnectedEvent(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getOtherUsername() {
        return otherUsername;
    }
}
