package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Equity;
import com.plexobject.dp.sample.domain.Option;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.domain.SharedMeta;

public class SecurityMarshaller implements DataRowSetMarshaller<Security> {
    private static MetaField securityId = MetaFieldFactory.create("securityId",
            Security.class.getSimpleName(), MetaFieldType.SCALAR_INTEGER, true);
    private static MetaField description = MetaFieldFactory.createText(
            "security.description", Security.class.getSimpleName(), false);
    private static MetaField type = MetaFieldFactory.createText(
            "security.type", Security.class.getSimpleName(), false);
    private static MetaField beta = MetaFieldFactory.createRowset("beta",
            Security.class.getSimpleName(), false);

    private static MetaField dividendRate = MetaFieldFactory.createDecimal(
            "security.dividendRate", Security.class.getSimpleName(), false);
    private static MetaField dividendInterval = MetaFieldFactory.createDecimal(
            "security.dividendInterval", Security.class.getSimpleName(), false);
    private static MetaField exDividendDate = MetaFieldFactory.createDate(
            "security.exDividendDate", Security.class.getSimpleName(), false);

    private static MetaField optionRoot = MetaFieldFactory.createRowset(
            "security.optionRoot", Security.class.getSimpleName(), false);
    private static MetaField optionType = MetaFieldFactory.createText(
            "security.optionType", Security.class.getSimpleName(), false);
    private static MetaField strikePrice = MetaFieldFactory.createDecimal(
            "security.strikePrice", Security.class.getSimpleName(), false);
    private static MetaField expirationDate = MetaFieldFactory.createDate(
            "security.expirationDate", Security.class.getSimpleName(), false);

    private static Metadata responseMeta = Metadata.from(securityId,
            SharedMeta.exchange, SharedMeta.symbol,
            SharedMeta.underlyingSymbol, description, type, beta, dividendRate,
            dividendInterval, exDividendDate, optionRoot, optionType,
            strikePrice, expirationDate);
    private static final BetaMarshaller betaMarshaller = new BetaMarshaller();
    private static final OptionRootMarshaller optionRootMarshaller = new OptionRootMarshaller();

    @Override
    public DataRowSet marshal(Security security) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(rowset, security, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Security> securities) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Security security : securities) {
            marshal(rowset, security, rowset.size());
        }
        return rowset;
    }

    private void marshal(DataRowSet rowset, Security security, int rowNum) {
        rowset.addValueAtRow(securityId, security.getId(), rowNum);
        rowset.addValueAtRow(SharedMeta.exchange, security.getExchange(),
                rowNum);
        rowset.addValueAtRow(SharedMeta.symbol, security.getSymbol(), rowNum);
        rowset.addValueAtRow(SharedMeta.underlyingSymbol,
                security.getUnderlyingSymbol(), rowNum);
        rowset.addValueAtRow(description, security.getDescription(), rowNum);
        rowset.addValueAtRow(type, security.getType().name(), rowNum);
        rowset.addValueAtRow(beta, betaMarshaller.marshal(security.getBeta()),
                rowNum);
        if (security instanceof Equity) {
            Equity equity = (Equity) security;
            rowset.addValueAtRow(dividendRate, equity.getDividendRate(), rowNum);
            rowset.addValueAtRow(dividendInterval,
                    equity.getDividendInterval(), rowNum);
            rowset.addValueAtRow(exDividendDate, equity.getExDividendDate(),
                    rowNum);
        } else if (security instanceof Option) {
            Option option = (Option) security;
            rowset.addValueAtRow(optionRoot,
                    optionRootMarshaller.marshal(option.getOptionRoot()),
                    rowNum);
            rowset.addValueAtRow(optionType, option.getOptionType().name(),
                    rowNum);
            rowset.addValueAtRow(strikePrice, option.getStrikePrice(), rowNum);
            rowset.addValueAtRow(expirationDate, option.getExpirationDate(),
                    rowNum);
        }
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
