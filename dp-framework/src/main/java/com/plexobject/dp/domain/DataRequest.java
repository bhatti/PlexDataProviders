package com.plexobject.dp.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * This class defines parameters for data query request
 * 
 * @author shahzad bhatti
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataRequest {
    private static final String RESPONSE_FIELDS = "fields";
    private DataRowSet parameters;
    private Metadata fields;
    private QueryConfiguration config;

    public DataRequest() {
    }

    public DataRequest(DataRowSet parameters, Metadata fields,
            QueryConfiguration config) {
        this.parameters = parameters;
        this.fields = fields;
        this.config = config;
    }

    public static DataRequest from(final Map<String, Object> args) {
        final DataRowSet parameters = new DataRowSet(Metadata.from());
        final Metadata fields = Metadata.from();
        final QueryConfiguration config = QueryConfiguration.from(args);
        for (Map.Entry<String, Object> e : args.entrySet()) {
            if (RESPONSE_FIELDS.equals(e.getKey())) {
                String[] fieldNames = e.getValue().toString().split(",");
                for (String fieldName : fieldNames) {
                    MetaField field = MetaFieldFactory.lookup(fieldName);
                    if (field != null) {
                        fields.addMetaField(field);
                    }
                }
            } else {
                MetaField field = MetaFieldFactory.lookup(e.getKey());
                if (field != null) {
                    parameters.addValueAtRow(field, e.getValue(), 0);
                }
            }
        }
        return new DataRequest(parameters, fields, config);
    }

    /**
     * accessor for request fields 
     */
    public DataRowSet getParameters() {
        return parameters;
    }

    /**
     * setter for request fields 
     */
    public void setParameters(DataRowSet parameters) {
        this.parameters = parameters;
    }

    /**
     * accessor for response fields 
     */
    public Metadata getFields() {
        return fields;
    }

    /**
     * setter for response fields 
     */
    public void setFields(Metadata fields) {
        this.fields = fields;
    }

    public QueryConfiguration getConfig() {
        return config;
    }

    public void setConfig(QueryConfiguration config) {
        this.config = config;
    }

}
