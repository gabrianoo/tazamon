package com.gability.tazamon.client.dav;

import com.gability.tazamon.client.Processor;
import com.gability.tazamon.client.dav.xml.MultiStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;

import static com.gability.tazamon.client.dav.ContentType.APPLICATION_XML;
import static com.gability.tazamon.client.dav.HttpHeader.AUTHORIZATION;
import static com.gability.tazamon.client.dav.HttpHeader.DEPTH;
import static com.gability.tazamon.client.dav.HttpMethod.PROPFIND;

/**
 * Implementation of {@link DavTazamonExecutor} using the PROPFIND HTTP method.
 */
@Slf4j
@Named
public final class PropFindDavTazamonExecutor implements DavTazamonExecutor {

    private static final String DEPTH_VALUE = "1";
    private final OkHttpClient okHttpClient;
    private final Processor processor;

    @Inject
    public PropFindDavTazamonExecutor(
            OkHttpClient okHttpClient,
            Processor processor
    ) {
        this.okHttpClient = okHttpClient;
        this.processor = processor;
    }

    @Override
    public Optional<DavTazamonResponse> execute(@NonNull DavTazamonRequest davTazamonRequest) {
        return Optional.of(davTazamonRequest)
                .map(this::executePropFindRequest)
                .flatMap(
                        element -> processor.from(element, MultiStatus.class)
                ).map(
                        multiStatus -> new DefaultDavTazamonResponse(multiStatus, davTazamonRequest)
                );
    }

    private String executePropFindRequest(DavTazamonRequest davTazamonRequest) {
        String responsePayload = null;
        try {
            RequestBody body = RequestBody.create(
                    MediaType.parse(APPLICATION_XML.getMediaType()),
                    davTazamonRequest.getRequestBody()
            );
            Request request = new Request.Builder()
                    .url(davTazamonRequest.getServerUrl())
                    .addHeader(AUTHORIZATION.getHeaderKey(), davTazamonRequest.getBase64EncodeAuthToken())
                    .addHeader(DEPTH.getHeaderKey(), DEPTH_VALUE)
                    .method(PROPFIND.toString(), body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            responsePayload = response.body().string();
        } catch (IOException e) {
            log.error(e.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("", e);
            }
        }
        return responsePayload;
    }
}
