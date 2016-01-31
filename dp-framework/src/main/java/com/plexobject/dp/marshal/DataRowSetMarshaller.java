package com.plexobject.dp.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;

public interface DataRowSetMarshaller<F> extends Marshaller<F, DataRowSet> {
    Metadata getMetadata();
}
