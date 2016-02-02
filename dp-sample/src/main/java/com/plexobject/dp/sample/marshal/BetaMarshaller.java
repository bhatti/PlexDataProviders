package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Beta;

public class BetaMarshaller implements DataRowSetMarshaller<Beta> {
    private static MetaField betaId = MetaFieldFactory.createDecimal(
            "beta.beta", Beta.class.getSimpleName(), false);
    private static MetaField month = MetaFieldFactory.createInteger(
            "beta.month", Beta.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(betaId, month);

    @Override
    public DataRowSet marshal(Beta beta) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(beta, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Beta> betas) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Beta beta : betas) {
            marshal(beta, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(Beta beta, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(betaId, beta.getBeta(), rowNum);
        rowset.addValueAtRow(month, beta.getMonth(), rowNum);
    }

    @Override
    public Beta unmarshal(DataRowSet rowset) {
        Beta beta = new Beta();
        return beta; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
