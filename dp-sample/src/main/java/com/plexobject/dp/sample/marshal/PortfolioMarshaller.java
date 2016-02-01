package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Portfolio;

public class PortfolioMarshaller implements DataRowSetMarshaller<Portfolio> {
    private static MetaField portfolioId = MetaFieldFactory.create(
            "portfolioId", MetaFieldType.SCALAR_INTEGER);
    private static MetaField cash = MetaFieldFactory.create("portfolio.cash",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField margin = MetaFieldFactory.create(
            "portfolio.margin", MetaFieldType.SCALAR_DECIMAL);
    private static Metadata responseMeta = Metadata.from(portfolioId, cash,
            margin);

    @Override
    public DataRowSet marshal(Portfolio portfolio) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(portfolio, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Portfolio> portfolios) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Portfolio portfolio : portfolios) {
            marshal(portfolio, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(Portfolio portfolio, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(portfolioId, portfolio.getId(), rowNum);
        rowset.addValueAtRow(cash, portfolio.getCash(), rowNum);
        rowset.addValueAtRow(margin, portfolio.getMargin(), rowNum);
    }

    @Override
    public Portfolio unmarshal(DataRowSet rowset) {
        Portfolio portfolio = new Portfolio();
        return portfolio; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
