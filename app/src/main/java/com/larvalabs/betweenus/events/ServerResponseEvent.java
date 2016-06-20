package com.larvalabs.betweenus.events;

public class ServerResponseEvent {
    private String log;

    public ServerResponseEvent(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
