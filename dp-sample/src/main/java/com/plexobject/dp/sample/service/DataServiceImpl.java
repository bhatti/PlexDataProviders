package com.plexobject.dp.sample.service;

import java.util.Collection;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.json.DataProviderDeserializer;
import com.plexobject.dp.json.DataProviderSerializer;
import com.plexobject.dp.json.DataRowDeserializer;
import com.plexobject.dp.json.DataRowSerializer;
import com.plexobject.dp.json.DataRowSetDeserializer;
import com.plexobject.dp.json.DataRowSetSerializer;
import com.plexobject.dp.json.MetadataDeserializer;
import com.plexobject.dp.json.MetadataSerializer;
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
import com.plexobject.dp.sample.provider.SymbolSearchProvider;
import com.plexobject.dp.sample.provider.SymbolsProvider;
import com.plexobject.dp.sample.provider.UsersByIdsProvider;
import com.plexobject.dp.sample.provider.UsersProvider;
import com.plexobject.dp.sample.provider.WatchlistByUserProvider;
import com.plexobject.encode.CodecConfigurer;
import com.plexobject.encode.CodecType;
import com.plexobject.encode.ObjectCodecFactory;
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
        dataProviderLocator.register(new SymbolSearchProvider());
        ObjectCodecFactory.getInstance().getObjectCodec(CodecType.JSON)
                .setCodecConfigurer(new CodecConfigurer() {
                    @Override
                    public void configureCodec(Object underlyingEncoder) {
                        if (underlyingEncoder instanceof ObjectMapper) {
                            ObjectMapper mapper = (ObjectMapper) underlyingEncoder;
                            SimpleModule module = new SimpleModule();
                            module.addSerializer(DataRow.class,
                                    new DataRowSerializer(DataRow.class));
                            module.addSerializer(DataRowSet.class,
                                    new DataRowSetSerializer(DataRowSet.class));
                            module.addSerializer(Metadata.class,
                                    new MetadataSerializer(Metadata.class));
                            module.addSerializer(DataProvider.class,
                                    new DataProviderSerializer(
                                            DataProvider.class));
                            //
                            module.addDeserializer(DataRow.class,
                                    new DataRowDeserializer());
                            module.addDeserializer(DataRowSet.class,
                                    new DataRowSetDeserializer());
                            module.addDeserializer(Metadata.class,
                                    new MetadataDeserializer());
                            module.addDeserializer(DataProvider.class,
                                    new DataProviderDeserializer(
                                            dataProviderLocator));
                            //
                            mapper.registerModule(module);

                        }
                    }
                });
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
