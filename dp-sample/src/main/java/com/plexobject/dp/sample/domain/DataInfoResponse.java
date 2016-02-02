package com.plexobject.dp.sample.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.plexobject.dp.domain.Metadata;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataInfoResponse {
    private Metadata requestMetadata;
    private Metadata responseMetadata;

    DataInfoResponse() {

    }

    public DataInfoResponse(Metadata requestMetadata, Metadata responseMetadata) {
        this.requestMetadata = requestMetadata;
        this.responseMetadata = responseMetadata;
    }

    public Metadata getRequestMetadata() {
        return requestMetadata;
    }

    public void setRequestMetadata(Metadata requestMetadata) {
        this.requestMetadata = requestMetadata;
    }

    public Metadata getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(Metadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }

    @Override
    public String toString() {
        return "DataInfoResponse [requestMetadata=" + requestMetadata
                + ", responseMetadata=" + responseMetadata + "]";
    }

}
