package com.plexobject.dp.query;

import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;

/**
 * This interface produces response fields given request fields via data
 * providers that provide those response fields.
 * 
 * Note: When someone calls query method, this QueryEngine will use all
 * registered data providers to aggregate the output fields. The implementation
 * may execute necessary data providers serially or in parallel depending on the
 * available input fields.
 * 
 * @author shahzad bhatti
 *
 */
public interface QueryEngine {
    /**
     * This method will query set of data fields given input
     * 
     * @param request
     *            - input parameter fields
     * @return response containing output data rows and errors (if occurred)
     */
    DataResponse query(DataRequest request);
}
