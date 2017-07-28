package com.gability.tazamon.client.dav;

public enum ContentType {

    APPLICATION_XML("application/xml");

    private String mediaType;

    ContentType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
