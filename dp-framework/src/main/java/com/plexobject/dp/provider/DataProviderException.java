package com.plexobject.dp.provider;

import com.plexobject.dp.domain.DataFieldRowSet;

/**
 * This exception is thrown when an error occurs in the data provider
 * 
 * @author shahzad bhatti
 *
 */
public class DataProviderException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final DataFieldRowSet requestFields;
    private final DataFieldRowSet responseFields;

    public DataProviderException(String message, Throwable cause,
            DataFieldRowSet requestFields, DataFieldRowSet responseFields) {
        super(message, cause);
        this.requestFields = requestFields;
        this.responseFields = responseFields;
    }

    public DataProviderException(String message, DataFieldRowSet requestFields,
            DataFieldRowSet responseFields) {
        super(message);
        this.requestFields = requestFields;
        this.responseFields = responseFields;
    }

    public DataFieldRowSet getRequestFields() {
        return requestFields;
    }

    public DataFieldRowSet getResponseFields() {
        return responseFields;
    }

}
