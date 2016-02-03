package com.plexobject.dp.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * This class defines response for output data fields
 * 
 * @author shahzad bhatti
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataResponse {
    private DataRowSet responseFields;
    private Map<String, Throwable> errorsByProviderName;
    private Set<String> providers = new HashSet<>();

    public DataResponse() {

    }

    public DataResponse(DataRowSet responseFields, Set<String> providers,
            Map<String, Throwable> errors) {
        this.responseFields = responseFields;
        this.providers = providers;
        this.errorsByProviderName = errors;
    }

    public DataRowSet getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(DataRowSet responseFields) {
        this.responseFields = responseFields;
    }

    public Map<String, Throwable> getErrorsByProviderName() {
        return errorsByProviderName;
    }

    public void setErrorsByProviderName(Map<String, Throwable> errors) {
        this.errorsByProviderName = errors;
    }

    public Set<String> getProviders() {
        return providers;
    }

    public void setProviders(Set<String> providers) {
        this.providers = providers;
    }

}
