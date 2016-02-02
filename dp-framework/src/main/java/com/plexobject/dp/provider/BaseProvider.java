package com.plexobject.dp.provider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class BaseProvider implements DataProvider {
    private final String name;
    private final Metadata mandatoryRequestFields;
    private final Metadata optionalRequestFields;
    private final Metadata responseFields;
    private TaskGranularity taskGranularity = TaskGranularity.COARSE;
    private int rank;

    public BaseProvider(String name, Metadata mandatoryRequestFields,
            Metadata optionalRequestFields, Metadata responseFields) {
        this.name = name;
        this.mandatoryRequestFields = mandatoryRequestFields;
        this.optionalRequestFields = optionalRequestFields;
        this.responseFields = responseFields;
    }

    public Metadata getMandatoryRequestFields() {
        return mandatoryRequestFields;
    }

    public Metadata getOptionalRequestFields() {
        return optionalRequestFields;
    }

    public Metadata getResponseFields() {
        return responseFields;
    }

    public TaskGranularity getTaskGranularity() {
        return taskGranularity;
    }

    public void setTaskGranularity(TaskGranularity taskGranularity) {
        this.taskGranularity = taskGranularity;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return name;
    }

    protected int addRowSet(DataRowSet response, DataRowSet rowset, int rowNum) {
        for (int i = 0; i < rowset.size(); i++) {
            for (MetaField field : response.getMetadata().getMetaFields()) {
                if (getResponseFields().contains(field)) {
                    response.addValueAtRow(field, rowset.getValue(field, i),
                            rowNum);
                }
            }
            rowNum++;
        }
        return rowNum;
    }
}
