package com.tazamon.client.dav.common;

public enum Status {

    OK("HTTP/1.1 200 OK"),
    NOT_FOUND("HTTP/1.1 404 Not Found");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
