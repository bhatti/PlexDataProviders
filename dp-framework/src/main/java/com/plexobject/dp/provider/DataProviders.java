package com.plexobject.dp.provider;

import java.util.Map;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataFieldRowSet;

/**
 * This interface produces response fields given request fields via data
 * providers that provide those response fields.
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
