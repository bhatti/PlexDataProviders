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
     * This method returns required request meta data
     * 
     * @return
     */
    Metadata getMandatoryRequestMetadata();

    /**
     * This method returns optional request meta data
     * 
     * @return
     */
    Metadata getOptionalRequestMetadata();

    /**
     * This method returns response meta data information
     * 
     * @return
     */
    Metadata getResponseMetadata();

    /**
     * This method returns granularity of task so that a separate thread can be
     * used to execute it
     * 
     * @return
     */
    TaskGranularity getTaskGranularity();
}
