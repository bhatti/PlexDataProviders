package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Equity;
import com.plexobject.dp.sample.domain.Option;
import com.plexobject.dp.sample.domain.Security;

public class SecurityMarshaller implements DataRowSetMarshaller<Security> {
    private static MetaField securityId = MetaFieldFactory.create("securityId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField exchange = MetaFieldFactory.create("exchange",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField underlyingSymbol = MetaFieldFactory.create(
            "underlyingSymbol", MetaFieldType.SCALAR_TEXT);
    private static MetaField description = MetaFieldFactory.create(
            "security.description", MetaFieldType.SCALAR_TEXT);
    private static MetaField type = MetaFieldFactory.create("security.type",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField beta = MetaFieldFactory.create("beta",
            MetaFieldType.SCALAR_OBJECT);

    private static MetaField dividendRate = MetaFieldFactory.create(
            "security.dividendRate", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField dividendInterval = MetaFieldFactory.create(
            "security.dividendInterval", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField exDividendDate = MetaFieldFactory.create(
            "security.exDividendDate", MetaFieldType.SCALAR_DATE);

    private static MetaField optionRoot = MetaFieldFactory.create(
            "security.optionRoot", MetaFieldType.SCALAR_OBJECT);
    private static MetaField optionType = MetaFieldFactory.create(
            "security.optionType", MetaFieldType.SCALAR_TEXT);
    private static MetaField strikePrice = MetaFieldFactory.create(
            "security.strikePrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField expirationDate = MetaFieldFactory.create(
            "security.expirationDate", MetaFieldType.SCALAR_DATE);

    private static Metadata responseMeta = Metadata.from(securityId, exchange,
            symbol, underlyingSymbol, description, type, beta, dividendRate,
            dividendInterval, exDividendDate, optionRoot, optionType,
            strikePrice, expirationDate);

    @Override
    public DataRowSet marshal(Security security) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(securityId, security.getId(), 0);
        rowset.addValueAtRow(exchange, security.getExchange(), 0);
        rowset.addValueAtRow(symbol, security.getSymbol(), 0);
        rowset.addValueAtRow(underlyingSymbol, security.getUnderlyingSymbol(),
                0);
        rowset.addValueAtRow(description, security.getDescription(), 0);
        rowset.addValueAtRow(type, security.getType(), 0);
        rowset.addValueAtRow(beta, security.getBeta(), 0);
        if (security instanceof Equity) {
            Equity equity = (Equity) security;
            rowset.addValueAtRow(dividendRate, equity.getDividendRate(), 0);
            rowset.addValueAtRow(dividendInterval,
                    equity.getDividendInterval(), 0);
            rowset.addValueAtRow(exDividendDate, equity.getExDividendDate(), 0);
        } else if (security instanceof Option) {
            Option option = (Option) security;
            rowset.addValueAtRow(optionRoot, option.getOptionRoot(), 0);
            rowset.addValueAtRow(optionType, option.getOptionType(), 0);
            rowset.addValueAtRow(strikePrice, option.getStrikePrice(), 0);
            rowset.addValueAtRow(expirationDate, option.getExpirationDate(), 0);
        }
        return rowset;
    }

    @Override
    public Security unmarshal(DataRowSet rowset) {
        Security security = new Equity();
        return security; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}
