package com.plexobject.dp.provider;

import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;

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
     * @param request
     *            - input parameter fields
     * @return response containing output data rows and errors (if occurred)
     */
    DataResponse produce(DataRequest request);
}
