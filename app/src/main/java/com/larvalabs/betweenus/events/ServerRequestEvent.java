package com.larvalabs.betweenus.events;

public class ServerRequestEvent {

    private String log;

    public ServerRequestEvent(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
