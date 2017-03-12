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
    private DataRowSet fields;
    private Map<String, Throwable> errorsByProviderName;
    private Set<String> providers = new HashSet<>();

    public DataResponse() {

    }

    public DataResponse(DataRowSet fields, Set<String> providers,
            Map<String, Throwable> errors) {
        this.fields = fields;
        this.providers = providers;
        this.errorsByProviderName = errors;
    }

    public DataRowSet getFields() {
        return fields;
    }

    public void setFields(DataRowSet fields) {
        this.fields = fields;
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
