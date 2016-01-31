package com.plexobject.dp.provider;

import com.plexobject.dp.domain.DataConfiguration;
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
     * @param requestFields
     *            - input parameter fields
     * @param responseFields
     *            - output fields
     * @param config
     *            - configuration parameters
     * @return
     * @throws DataProviderException
     */
    void produce(DataRowSet requestFields, DataRowSet responseFields,
            DataConfiguration config) throws DataProviderException;
}
