package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.CompanyEvent;

public class CompanyEventMarshaller implements
        DataRowSetMarshaller<CompanyEvent> {
    private static MetaField companyEventId = MetaFieldFactory.create(
            "companyEventId", MetaFieldType.SCALAR_INTEGER, true);
    private static MetaField companyEventType = MetaFieldFactory
            .createText("companyEvent.type");
    private static MetaField companyEventName = MetaFieldFactory
            .createText("companyEvent.name");
    private static MetaField companyEventDate = MetaFieldFactory
            .createDate("companyEvent.date");
    private static Metadata responseMeta = Metadata.from(companyEventId,
            companyEventType, companyEventName, companyEventDate);

    @Override
    public DataRowSet marshal(CompanyEvent event) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(companyEventId, event.getId(), 0);
        rowset.addValueAtRow(companyEventType, event.getType().name(), 0);
        rowset.addValueAtRow(companyEventName, event.getName(), 0);
        rowset.addValueAtRow(companyEventDate, event.getDate(), 0);
        return rowset;
    }

    @Override
    public CompanyEvent unmarshal(DataRowSet rowset) {
        CompanyEvent event = new CompanyEvent();
        return event; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}