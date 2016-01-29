package com.plexobject.dp.provider;

import java.util.Map;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataFieldRowSet;

/**
 * This interface extends DataProvider and defines methods for
 * registering/unregistering as well as finding input data fields for output
 * fields
 * 
 * Note: When someone calls produce method, this DataProvider will use all
 * registered data providers to produce the output fields. The implementation
 * may execute necessary data providers serially or in parallel depending on the
 * available input fields.
 * 
 * @author shahzad bhatti
 *
 */
public interface DataProviders {

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
     * This method will produce set of data fields given input
     * 
     * @param requestFields
     *            - input parameter fields
     * @param responseFields
     *            - output fields
     * @param config
     *            - configuration parameters
     * @param errorHandler
     *            - error handler
     * @return errors from providers if exist
     */
    Map<DataProvider, Throwable> produce(DataFieldRowSet requestFields,
            DataFieldRowSet responseFields, DataConfiguration config);
}
