package com.plexobject.dp.provider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class BaseProvider implements DataProvider {
    private final String name;
    private final Metadata mandatoryRequestMetadata;
    private final Metadata optionalRequestMetadata;
    private final Metadata responseMetadata;
    private TaskGranularity taskGranularity = TaskGranularity.COARSE;
    private int rank;

    public BaseProvider(String name, Metadata mandatoryRequestMetadata,
            Metadata optionalRequestMetadata, Metadata responseMetadata) {
        this.name = name;
        this.mandatoryRequestMetadata = mandatoryRequestMetadata;
        this.optionalRequestMetadata = optionalRequestMetadata;
        this.responseMetadata = responseMetadata;
    }

    @Override
    public Metadata getMandatoryRequestMetadata() {
        return mandatoryRequestMetadata;
    }

    @Override
    public Metadata getOptionalRequestMetadata() {
        return optionalRequestMetadata;
    }

    @Override
    public Metadata getResponseMetadata() {
        return responseMetadata;
    }

    @Override
    public TaskGranularity getTaskGranularity() {
        return taskGranularity;
    }

    public void setTaskGranularity(TaskGranularity taskGranularity) {
        this.taskGranularity = taskGranularity;
    }

    @Override
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
        for (MetaField requestField : mandatoryRequestMetadata.getMetaFields()) {
            if (other.getResponseMetadata().contains(requestField)) {
                return +1;
            }
        }
        for (MetaField responseField : responseMetadata.getMetaFields()) {
            if (other.getMandatoryRequestMetadata().contains(responseField)) {
                return -1;
            }
        }
        for (MetaField responseField : responseMetadata.getMetaFields()) {
            if (other.getOptionalRequestMetadata().contains(responseField)) {
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
                if (getResponseMetadata().contains(field)) {
                    response.addValueAtRow(field, rowset.getValue(field, i),
                            rowNum);
                }
            }
            rowNum++;
        }
        return rowNum;
    }
}
