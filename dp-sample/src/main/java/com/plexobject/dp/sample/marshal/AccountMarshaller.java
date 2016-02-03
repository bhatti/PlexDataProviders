package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.AccountType;
import com.plexobject.dp.sample.domain.SharedMeta;

public class AccountMarshaller implements DataRowSetMarshaller<Account> {
    private static MetaField accountType = MetaFieldFactory.createText(
            "account.type", Account.class.getSimpleName(), false);
    private static MetaField accountName = MetaFieldFactory.createText(
            "account.name", Account.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(accountType,
            accountName);

    @Override
    public DataRowSet marshal(Account account) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(rowset, account, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Account> accounts) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Account account : accounts) {
            marshal(rowset, account, rowset.size());
        }
        return rowset;
    }

    private void marshal(DataRowSet rowset, Account account, int rowNum) {
        rowset.addValueAtRow(SharedMeta.accountId, account.getId(), rowNum);
        rowset.addValueAtRow(accountType, account.getAccountType().name(),
                rowNum);
        rowset.addValueAtRow(accountName, account.getAccountName(), rowNum);
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
