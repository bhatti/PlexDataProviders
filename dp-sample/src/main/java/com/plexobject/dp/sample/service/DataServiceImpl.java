package com.plexobject.dp.sample.service;

import java.util.Collection;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderLocator;
import com.plexobject.dp.provider.DataProviders;
import com.plexobject.dp.provider.impl.DataProviderLocatorImpl;
import com.plexobject.dp.provider.impl.DataProvidersImpl;
import com.plexobject.dp.sample.provider.AccountsByIdsProvider;
import com.plexobject.dp.sample.provider.AccountsByUseridProvider;
import com.plexobject.dp.sample.provider.CompaniesBySymbolsProvider;
import com.plexobject.dp.sample.provider.OrdersByAccountIdsProvider;
import com.plexobject.dp.sample.provider.PositionGroupsBySymbolsProvider;
import com.plexobject.dp.sample.provider.PositionsBySymbolsProvider;
import com.plexobject.dp.sample.provider.QuotesBySymbolsProvider;
import com.plexobject.dp.sample.provider.SecuritiesBySymbolsProvider;
import com.plexobject.dp.sample.provider.SymbolsProvider;
import com.plexobject.dp.sample.provider.UsersByIdsProvider;
import com.plexobject.dp.sample.provider.UsersProvider;
import com.plexobject.dp.sample.provider.WatchlistByUserProvider;
import com.plexobject.handler.Request;

@WebService
@Path("/data")
public class DataServiceImpl implements DataService {
    private DataProviderLocator dataProviderLocator = new DataProviderLocatorImpl();
    private DataProviders dataProviders = new DataProvidersImpl(
            dataProviderLocator);

    public DataServiceImpl() {
        dataProviderLocator.register(new AccountsByIdsProvider());
        dataProviderLocator.register(new AccountsByUseridProvider());
        dataProviderLocator.register(new CompaniesBySymbolsProvider());
        dataProviderLocator.register(new OrdersByAccountIdsProvider());
        dataProviderLocator.register(new PositionGroupsBySymbolsProvider());
        dataProviderLocator.register(new PositionsBySymbolsProvider());
        dataProviderLocator.register(new QuotesBySymbolsProvider());
        dataProviderLocator.register(new SecuritiesBySymbolsProvider());
        dataProviderLocator.register(new UsersByIdsProvider());
        dataProviderLocator.register(new WatchlistByUserProvider());
        dataProviderLocator.register(new SymbolsProvider());
        dataProviderLocator.register(new UsersProvider());
    }

    @Override
    @GET
    public DataResponse query(Request webRequest) {
        final DataRequest dataRequest = DataRequest.from(webRequest
                .getProperties());
        //
        return dataProviders.produce(dataRequest);
    }

    @Override
    @GET
    @Path("/info")
    public Collection<DataProvider> info() {
        return dataProviderLocator.getAll();
    }
}