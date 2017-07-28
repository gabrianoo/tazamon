package com.gability.tazamon.client.dav;

public enum Status {

    OK("HTTP/1.1 200 OK"),
    NOT_FOUND("HTTP/1.1 404 Not Found");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
