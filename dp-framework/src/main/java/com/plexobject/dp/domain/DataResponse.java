package com.plexobject.dp.domain;

import java.util.Map;

import com.plexobject.dp.provider.DataProvider;

public class DataResponse {
    private DataRowSet responseFields;
    private Map<DataProvider, Throwable> errors;

    public DataResponse() {

    }

    public DataResponse(DataRowSet responseFields,
            Map<DataProvider, Throwable> errors) {
        this.responseFields = responseFields;
        this.errors = errors;
    }

    public DataRowSet getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(DataRowSet responseFields) {
        this.responseFields = responseFields;
    }

    public Map<DataProvider, Throwable> getErrors() {
        return errors;
    }

    public void setErrors(Map<DataProvider, Throwable> errors) {
        this.errors = errors;
    }

}
