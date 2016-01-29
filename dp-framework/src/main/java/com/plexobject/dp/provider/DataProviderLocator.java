package com.plexobject.dp.provider;

import java.util.Collection;

import com.plexobject.dp.domain.Metadata;

/**
 * This interface is used to find data providers given request data and response
 * data needed. Also, it defines methods for registering/unregistering as well
 * as finding input data fields for output fields
 * 
 * @author shahzad bhatti
 *
 */
public interface DataProviderLocator {
    /**
     * This method will register data provider that requires input fields and
     * produces output fields
     * 
     * @param provider
     * @param input
     * @param output
     */
    void register(DataProvider provider);

    /**
     * This method will unregister data provider
     * 
     * @param provider
     */
    void unregister(DataProvider provider);

    /**
     * This method returns data-providers that produce the required output
     * fields passed as parameter.
     * 
     * @param requestFields
     * @param responseFields
     * @return collection of data providers
     */
    Collection<DataProvider> locate(Metadata requestFields,
            Metadata responseFields);
}
