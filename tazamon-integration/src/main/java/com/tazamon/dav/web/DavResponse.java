package com.tazamon.dav.web;

import com.tazamon.dav.common.XmlProcessor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Optional;

/**
 * The goal of this {@link DavResponse} is to wrap the Web Dav response for safe refactoring/maintaining later.
 */
@Slf4j
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DavResponse {

    private DavPropertySet davPropertySet;

    public Optional<Element> lookUpDavProperty(String davPropertyName) {
        DavProperty<?> davProperty = davPropertySet.get(davPropertyName);
        Optional<Element> optionalDocument = Optional.empty();
        try {
            if (davProperty != null) {
                optionalDocument = Optional.of(davProperty.toXml(XmlProcessor.buildXmlNode()));
            }
        } catch (ParserConfigurationException e) {
            log.error("", e);
        }
        return optionalDocument;
    }


    /**
     * A builder class for {@link DavResponse}.
     */
    public static class ResponseWrapperBuilder {

        private DavPropertySet davPropertySet;

        private ResponseWrapperBuilder() {
        }

        /**
         * This method is used to set the {@link DavPropertySet} to extract information from it later.
         *
         * @param davPropertySet used to set the {@link DavPropertySet}
         */
        public ResponseWrapperBuilder davPropertySet(DavPropertySet davPropertySet) {
            this.davPropertySet = davPropertySet;
            return this;
        }

        /**
         * Build the {@link DavResponse} using the provided data in the builder.
         *
         * @return {@link DavResponse} ready to be used by {@link WebDavRequest} implementations.
         */
        public DavResponse build() {
            return new DavResponse(davPropertySet);
        }
    }

    /**
     * Provide a new {@link DavResponse.ResponseWrapperBuilder} to assist in building a {@link DavResponse}
     * instance ready to be used by {@link WebDavRequest} implementations.
     *
     * @return new empty instance of {@link DavResponse.ResponseWrapperBuilder}.
     */
    public static DavResponse.ResponseWrapperBuilder builder() {
        return new DavResponse.ResponseWrapperBuilder();
    }
}
