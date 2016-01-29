package com.plexobject.dp.provider;

import com.plexobject.dp.domain.Metadata;

/**
 * This interface defines a method for producing data fields given input
 * 
 * @author shahzad bhatti
 *
 */
public interface DataProvider extends DataProducer, Comparable<DataProvider> {
    /**
     * This method returns name of the provider
     * 
     * @return
     */
    String getName();

    /**
     * This method returns rank of provider when searching same data field
     * 
     * @return
     */
    int getRank();

    /**
     * This method returns required request fields
     * 
     * @return
     */
    Metadata getMandatoryRequestFields();

    /**
     * This method returns optional request fields
     * 
     * @return
     */
    Metadata getOptionalRequestFields();

    /**
     * This method returns response fields information
     * 
     * @return
     */
    Metadata getResponseFields();

    /**
     * This method returns granularity of task so that a separate thread can be
     * used to execute it
     * 
     * @return
     */
    TaskGranularity getTaskGranularity();
}
