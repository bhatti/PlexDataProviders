package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.OptionRoot;

public class OptionRootMarshaller implements DataRowSetMarshaller<OptionRoot> {
    private static MetaField opraRoot = MetaFieldFactory.create("opraRoot",
            MetaFieldType.SCALAR_TEXT, true);
    private static MetaField underlyingSymbol = MetaFieldFactory
            .createText("optionRoot.underlyingSymbol");
    private static MetaField multiplier = MetaFieldFactory
            .createDecimal("optionRoot.multiplier");
    private static MetaField excerciseStyle = MetaFieldFactory
            .createText("optionRoot.excerciseStyle");
    private static Metadata responseMeta = Metadata.from(opraRoot,
            underlyingSymbol, excerciseStyle, multiplier);

    @Override
    public DataRowSet marshal(OptionRoot optionRoot) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(optionRoot, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<OptionRoot> optionRoots) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (OptionRoot optionRoot : optionRoots) {
            marshal(optionRoot, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(OptionRoot optionRoot, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(opraRoot, optionRoot.getOpraRoot(), rowNum);
        rowset.addValueAtRow(underlyingSymbol, optionRoot.getUnderlying()
                .getSymbol(), rowNum);
        rowset.addValueAtRow(excerciseStyle, optionRoot.getExcerciseStyle(),
                rowNum);
        rowset.addValueAtRow(multiplier, optionRoot.getMultiplier(), rowNum);
    }

    @Override
    public OptionRoot unmarshal(DataRowSet rowset) {
        OptionRoot optionRoot = new OptionRoot();
        return optionRoot; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
