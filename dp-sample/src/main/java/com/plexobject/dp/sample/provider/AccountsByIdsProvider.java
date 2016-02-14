package com.plexobject.dp.sample.provider;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.marshal.AccountMarshaller;

public class AccountsByIdsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.accountId);
    private static Metadata optionalMeta = Metadata.from();
    private static AccountMarshaller marshaller = new AccountMarshaller();

    public AccountsByIdsProvider() {
        super("AccountsByIdsProvider", parameterMeta, optionalMeta, marshaller
                .getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            QueryConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            Long id = parameter.getValueAsLong(SharedMeta.accountId, i);
            Account account = DaoLocator.accountDao.getById(id);
            if (account != null) {
                DataRowSet rowset = marshaller.marshal(account);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
