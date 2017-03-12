package com.plexobject.dp.provider;

import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.domain.DataRowSet;

/**
 * This interface defines method for producing collection of data fields given
 * input fields
 * 
 * @author shahzad bhatti
 *
 */
public interface DataProducer {
    /**
     * This method will produce set of data fields given input
     * 
     * @param requestMetadata
     *            - input parameter fields
     * @param responseMetadata
     *            - output fields
     * @param config
     *            - configuration parameters
     * @return
     * @throws DataProviderException
     */
    void produce(DataRowSet requestMetadata, DataRowSet responseMetadata,
            QueryConfiguration config) throws DataProviderException;
}
