package com.plexobject.dp.provider;

import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFields;

public abstract class BaseProvider implements DataProvider {
    private final MetaFields mandatoryRequestFields;
    private final MetaFields optionalRequestFields;
    private final MetaFields responseFields;

    public BaseProvider(MetaFields mandatoryRequestFields,
            MetaFields optionalRequestFields, MetaFields responseFields) {
        this.mandatoryRequestFields = mandatoryRequestFields;
        this.optionalRequestFields = optionalRequestFields;
        this.responseFields = responseFields;
    }

    @Override
    public final MetaFields getMandatoryRequestFields() {
        return mandatoryRequestFields;
    }

    @Override
    public final MetaFields getOptionalRequestFields() {
        return optionalRequestFields;
    }

    @Override
    public final MetaFields getResponseFields() {
        return responseFields;
    }

    @Override
    public TaskGranularity getTaskGranularity() {
        return TaskGranularity.COARSE;
    }

    @Override
    public int compareTo(DataProvider other) {
        for (MetaField requestField : mandatoryRequestFields.getMetaFields()) {
            if (other.getResponseFields().contains(requestField)) {
                return +1;
            }
        }
        for (MetaField responseField : responseFields.getMetaFields()) {
            if (other.getMandatoryRequestFields().contains(responseField)) {
                return -1;
            }
        }
        for (MetaField responseField : responseFields.getMetaFields()) {
            if (other.getOptionalRequestFields().contains(responseField)) {
                return -1;
            }
        }
        return 0;
    }

}
