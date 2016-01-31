package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.AccountType;

public class AccountMarshaller implements DataRowSetMarshaller<Account> {
    private static MetaField accountId = MetaFieldFactory.create("accountId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField accountType = MetaFieldFactory.create(
            "account.type", MetaFieldType.SCALAR_TEXT);
    private static MetaField accountName = MetaFieldFactory.create(
            "account.name", MetaFieldType.SCALAR_TEXT);
    private static Metadata responseMeta = Metadata.from(accountType,
            accountName);

    @Override
    public DataRowSet marshal(Account account) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(accountId, account.getId(), 0);
        rowset.addValueAtRow(accountType, account.getAccountType().name(), 0);
        rowset.addValueAtRow(accountName, account.getAccountName(), 0);
        return rowset;
    }

    @Override
    public Account unmarshal(DataRowSet rowset) {
        Account account = new Account();
        account.setAccountType(AccountType.valueOf(rowset.getValueAsText(
                accountType, 0)));
        account.setAccountName(rowset.getValueAsText(accountName, 0));
        if (rowset.hasFieldValue(accountId, 0)) {
            account.setId(rowset.getValueAsLong(accountId, 0));
        }
        return account;
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
