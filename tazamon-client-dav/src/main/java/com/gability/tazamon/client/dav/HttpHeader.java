package com.gability.tazamon.client.dav;

public enum HttpHeader {

    AUTHORIZATION("Authorization"),
    DEPTH("Depth");

    private String headerKey;

    HttpHeader(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderKey() {
        return headerKey;
    }
}
