package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.AccountType;
import com.plexobject.dp.sample.domain.SharedMeta;

public class AccountMarshaller implements DataRowSetMarshaller<Account> {
    private static MetaField accountType = MetaFieldFactory
            .createText("account.type");
    private static MetaField accountName = MetaFieldFactory
            .createText("account.name");
    private static Metadata responseMeta = Metadata.from(accountType,
            accountName);

    @Override
    public DataRowSet marshal(Account account) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(SharedMeta.accountId, account.getId(), 0);
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
        if (rowset.hasFieldValue(SharedMeta.accountId, 0)) {
            account.setId(rowset.getValueAsLong(SharedMeta.accountId, 0));
        }
        return account;
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
