package com.plexobject.dp.domain;

import java.util.Map;

public class DataRequest {
    private static final String RESPONSE_FIELDS = "responseFields";
    private DataRowSet parameters;
    private Metadata responseFields;
    private DataConfiguration config;

    public DataRequest() {
    }

    public DataRequest(DataRowSet parameters, Metadata responseFields,
            DataConfiguration config) {
        this.parameters = parameters;
        this.responseFields = responseFields;
        this.config = config;
    }

    public static DataRequest from(final Map<String, Object> args) {
        final DataRowSet parameters = new DataRowSet(Metadata.from());
        final Metadata responseFields = Metadata.from();
        final DataConfiguration config = DataConfiguration.from(args);
        for (Map.Entry<String, Object> e : args.entrySet()) {
            if (RESPONSE_FIELDS.equals(e.getKey())) {
                String[] fieldNames = e.getValue().toString().split(",");
                for (String fieldName : fieldNames) {
                    MetaField field = MetaFieldFactory.lookup(fieldName);
                    if (field != null) {
                        responseFields.addMetaField(field);
                    }
                }
            } else {
                MetaField field = MetaFieldFactory.lookup(e.getKey());
                if (field != null) {
                    parameters.addValueAtRow(field, e.getValue(), 0);
                }
            }
        }
        return new DataRequest(parameters, responseFields, config);
    }

    public DataRowSet getParameters() {
        return parameters;
    }

    public void setParameters(DataRowSet parameters) {
        this.parameters = parameters;
    }

    public Metadata getResponseFields() {
        return responseFields;
    }

    public void setResponseFields(Metadata responseFields) {
        this.responseFields = responseFields;
    }

    public DataConfiguration getConfig() {
        return config;
    }

    public void setConfig(DataConfiguration config) {
        this.config = config;
    }

}
