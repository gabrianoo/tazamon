package com.tazamon.client.http;

import org.w3c.dom.Element;

import java.util.Optional;

public interface HttpTazamonResponsePropertyLookUp<T> {

    Optional<Element> lookUpProperty(HttpTazamonResponse<T> httpTazamonResponse, String propertyName);
}
