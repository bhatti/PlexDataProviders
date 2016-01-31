package com.plexobject.dp.provider.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.metrics.Metric;
import com.plexobject.dp.metrics.Timer;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderLocator;
import com.plexobject.dp.provider.DataProviders;
import com.plexobject.dp.provider.TaskGranularity;

/**
 * This class implements Data Providers interface
 * 
 * @author shahzad bhatti
 *
 */
public class DataProvidersImpl implements DataProviders {
    private static final Logger logger = Logger
            .getLogger(DataProvidersImpl.class);

    private final DataProviderLocator dataProviderLocator;
    private ExecutorService defaultExecutor;

    public DataProvidersImpl(DataProviderLocator dataProviderLocator) {
        this.dataProviderLocator = dataProviderLocator;
    }

    public ExecutorService getDefaultExecutor() {
        return defaultExecutor;
    }

    public void setDefaultExecutor(ExecutorService defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    @Override
    public DataResponse produce(DataRequest request) {
        // Get all data providers needed
        Collection<DataProvider> providers = dataProviderLocator.locate(request
                .getParameters().getMetadata(), request.getResponseFields());
        if (logger.isDebugEnabled()) {
            logger.debug("Executing requestFields " + request.getParameters()
                    + ", responseFields " + request.getResponseFields()
                    + " with " + providers);
        }
        DataRowSet responseFields = new DataRowSet(request.getResponseFields());
        //
        // creating parallel thread executor
        final ExecutorService executor = defaultExecutor != null ? defaultExecutor
                : Executors.newFixedThreadPool(getThreadPoolSize(providers));
        final Timer timer = Metric.newTimer("DataProvidersImpl.produce");
        try {
            Map<DataProvider, Throwable> errors = new ProvidersExecutor(
                    request.getParameters(), responseFields,
                    request.getConfig(), providers, executor).execute();
            return new DataResponse(responseFields, errors);
        } finally {
            if (executor != defaultExecutor) {
                executor.shutdown();
            }
            timer.stop();
        }
    }

    private static int getThreadPoolSize(Collection<DataProvider> providers) {
        int count = 0;
        for (DataProvider provider : providers) {
            if (provider.getTaskGranularity() == TaskGranularity.COARSE) {
                count++;
            }
        }
        return Math.min(count, 3) + 1;
    }

}
