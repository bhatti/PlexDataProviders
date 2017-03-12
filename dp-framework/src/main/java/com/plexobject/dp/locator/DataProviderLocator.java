package com.plexobject.dp.locator;

import java.util.Collection;

import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.DataProvider;

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
     * @param requestMeta
     * @param responseMeta
     * @return collection of data providers
     */
    Collection<DataProvider> locate(Metadata requestMeta, Metadata responseMeta);

    /**
     * This method returns all providers matching kinds
     * 
     * @param kinds
     *            - list of kinds or empty list for all
     * 
     * @return
     */
    Collection<DataProvider> getAllWithKinds(String... kinds);

    /**
     * This method returns all providers
     * 
     * @return
     */
    Collection<DataProvider> getAll();

    /**
     * This method finds provider with given name
     * 
     * @param name
     * @return
     */
    DataProvider getByName(String name);
}
