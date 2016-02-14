package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.Order;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.marshal.OrderMarshaller;

public class OrdersByAccountIdsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.accountId);
    private static Metadata optionalMeta = Metadata.from();
    private static OrderMarshaller marshaller = new OrderMarshaller();

    public OrdersByAccountIdsProvider() {
        super("OrdersByAccountIdsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            QueryConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final Long id = parameter.getValueAsLong(SharedMeta.accountId, i);
            Collection<Order> orders = DaoLocator.orderDao
                    .filter(new Filter<Order>() {
                        @Override
                        public boolean accept(Order object) {
                            return object.getAccount().getId().equals(id);
                        }
                    });
            //
            if (orders.size() > 0) {
                DataRowSet rowset = marshaller.marshal(orders);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
