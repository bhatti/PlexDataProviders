package com.plexobject.dp.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataFieldRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFields;

/**
 * This class implements Data Providers interface
 * 
 * @author shahzad bhatti
 *
 */
public class DataProvidersImpl implements DataProviders {
    private static final Logger logger = Logger
            .getLogger(DataProvidersImpl.class);

    private ConcurrentHashMap<MetaField, Set<DataProvider>> providersByOutputMetaField = new ConcurrentHashMap<>();
    private ExecutorService defaultExecutor;

    public ExecutorService getDefaultExecutor() {
        return defaultExecutor;
    }

    public void setDefaultExecutor(ExecutorService defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    @Override
    public void produce(final DataFieldRowSet requestFields,
            final DataFieldRowSet responseFields, DataConfiguration config)
            throws DataProviderException {
        // Get all data providers needed
        Collection<DataProvider> providers = getDataProviders(
                requestFields.getMetaFields(), responseFields.getMetaFields());
        //
        // creating parallel thread executor
        final ExecutorService executor = defaultExecutor != null ? defaultExecutor
                : Executors.newFixedThreadPool(getThreadPoolSize(providers));
        try {
            // Add intermediate data needed, e.g. we could call provider A that
            // returns some fields, which are used as input for provider B
            DataFieldRowSet optionalRequestFields = new DataFieldRowSet(
                    new MetaFields());
            addIntermediateAndOptionalFields(requestFields,
                    optionalRequestFields, responseFields, providers);
            if (config.getQueryTimeoutMillis() > 0) {
                Future<?> future = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        doProduce(requestFields, responseFields, config,
                                providers, executor, optionalRequestFields);
                    }
                });
                // wait for main task to be completed
                // this may throw TimeoutException
                while (!future.isDone() && !future.isCancelled()) {
                    try {
                        future.get(config.getQueryTimeoutMillis(),
                                TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    } catch (ExecutionException e) {
                        throw new DataProviderException(
                                "Failed to execute provider ", e,
                                requestFields, responseFields);
                    } catch (TimeoutException e) {
                        throw new DataProviderException(
                                "Failed to execute provider ", e,
                                requestFields, responseFields);
                    }
                }
            } else {
                doProduce(requestFields, responseFields, config, providers,
                        executor, optionalRequestFields);
            }
        } finally {
            executor.shutdown();
        }
    }

    @Override
    public void register(DataProvider provider) {
        for (MetaField outputField : provider.getResponseFields()
                .getMetaFields()) {
            synchronized (outputField) {
                Set<DataProvider> providers = providersByOutputMetaField
                        .get(outputField);
                if (providers == null) {
                    providers = new HashSet<DataProvider>();
                    Set<DataProvider> oldProviders = providersByOutputMetaField
                            .putIfAbsent(outputField, providers);
                    if (oldProviders != null) {
                        providers = oldProviders;
                    }
                }
                providers.add(provider);
            }
        }
    }

    @Override
    public void unregister(DataProvider provider) {
        for (MetaField outputField : provider.getResponseFields()
                .getMetaFields()) {
            synchronized (outputField) {
                Set<DataProvider> providers = providersByOutputMetaField
                        .get(outputField);
                if (providers != null) {
                    providers.remove(provider);
                    if (providers.size() == 0) {
                        providersByOutputMetaField.remove(outputField);
                    }
                }
            }
        }
    }

    /**
     * This method will return data-provider that produce the required output
     * fields passed as parameter.
     * 
     * @param requestFields
     * @param responseFields
     * @return collection of data providers
     */
    Collection<DataProvider> getDataProviders(MetaFields requestFields,
            MetaFields responseFields) {
        final List<DataProvider> providers = new ArrayList<>();
        populateDataProviders(new MetaFields(requestFields.getMetaFields()),
                new MetaFields(responseFields.getMetaFields()), providers);
        Collections.sort(providers); // sort by dependency
        return providers;
    }

    private void populateDataProviders(MetaFields requestFields,
            MetaFields responseFields, List<DataProvider> existingProviders) {
        responseFields.removeMetaFields(requestFields);
        for (MetaField responseField : responseFields.getMetaFields()) {
            Set<DataProvider> providers = providersByOutputMetaField
                    .get(responseField);
            if (providers == null) {
                throw new DataProviderException("Failed to find provider for "
                        + responseField, null, null);
            }
            DataProvider provider = getBestDataProvider(providers,
                    requestFields);
            if (existingProviders.contains(provider)) {
                continue;
            }
            existingProviders.add(provider);

            // find the missing fields from the mandatory request parameters and
            // we will try to find providers for those
            MetaFields missingFields = requestFields
                    .getMissingMetaFields(provider.getMandatoryRequestFields());

            // add output fields to requests so that we can use it for other
            // providers
            requestFields.addMetaFields(provider.getResponseFields());
            //
            if (missingFields.size() > 0) {
                populateDataProviders(requestFields, missingFields,
                        existingProviders);
            }
        }
    }

    // go through all providers and retrieve response data fields
    // note that some of providers may not have all request data fields so we
    // will wait until we have the request fields
    private void doProduce(final DataFieldRowSet requestFields,
            final DataFieldRowSet responseFields, DataConfiguration config,
            Collection<DataProvider> providers, final ExecutorService executor,
            DataFieldRowSet optionalRequestFields) {
        while (providers.size() > 0) {
            // Execute providers that have the request fields available
            Collection<DataProvider> executedProviders = executeProviders(
                    requestFields, optionalRequestFields, responseFields,
                    config, providers, executor);
            if (providers.size() > 0 && executedProviders.size() == 0) {
                logger.warn("Providers " + providers
                        + " cannot be fulfilled\n\trequestFields "
                        + requestFields + "\n\tresponseFields" + responseFields);
                throw new IllegalStateException("Providers " + providers
                        + " cannot be fulfilled\n\trequestFields "
                        + requestFields + "\n\tresponseFields" + responseFields);
            }
        }
    }

    // This method finds data provider that matches or almost matches the
    // request parameters that we have
    private DataProvider getBestDataProvider(Set<DataProvider> providers,
            MetaFields requestFields) {
        DataProvider bestProvider = null;
        int minCount = Integer.MAX_VALUE;
        for (DataProvider provider : providers) {
            int count = provider.getMandatoryRequestFields().getMissingCount(
                    requestFields);
            if (count == 0) {
                return provider;
            }
            if (count < minCount) {
                minCount = count;
                bestProvider = provider;
            }
        }
        return bestProvider;
    }

    // Go through all providers and if we have all the necessary request
    // parameters then we will execute it.
    private Collection<DataProvider> executeProviders(
            final DataFieldRowSet requestFields,
            DataFieldRowSet optionalRequestFields,
            final DataFieldRowSet responseFields,
            final DataConfiguration config,
            final Collection<DataProvider> providers,
            final ExecutorService executor) {
        final Collection<DataProvider> executedProviders = new ArrayList<>();
        final Collection<Future<?>> futures = new ArrayList<>();
        for (final DataProvider provider : providers) {
            if (requestFields.getMetaFields().getMissingCount(
                    provider.getMandatoryRequestFields()) == 0) {
                executedProviders.add(provider);
                if (provider.getTaskGranularity() == TaskGranularity.COARSE) {
                    // execute in background thread
                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            executeProvider(requestFields,
                                    optionalRequestFields, responseFields,
                                    config, provider);
                        }
                    });
                    futures.add(future);
                } else {
                    // execute in the same thread
                    executeProvider(requestFields, optionalRequestFields,
                            responseFields, config, provider);
                }
            }
        }
        providers.removeAll(executedProviders);
        // waiting for providers to finish
        for (Future<?> future : futures) {
            while (!future.isDone() && !future.isCancelled()) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.interrupted();
                } catch (ExecutionException e) {
                    throw new DataProviderException(
                            "Failed to execute provider ", e, requestFields,
                            responseFields);
                }
            }
        }
        return executedProviders;
    }

    private void executeProvider(final DataFieldRowSet requestFields,
            DataFieldRowSet optionalRequestFields,
            final DataFieldRowSet responseFields,
            final DataConfiguration config, final DataProvider provider) {
        try {
            // call data provider
            provider.produce(requestFields, responseFields, config);
            // add intermediate fields needed for future providers
            // add optional fields as request parameters as well
            for (int i = 0; i < responseFields.size(); i++) {
                for (MetaField responseField : provider.getResponseFields()
                        .getMetaFields()) {
                    if ((responseFields.getMetaFields().contains(responseField) && responseFields
                            .hasFieldValue(responseField, i))
                            || (optionalRequestFields.getMetaFields().contains(
                                    responseField) && responseFields
                                    .hasFieldValue(responseField, i))) {
                        Object value = responseFields
                                .getField(responseField, i);
                        requestFields.addDataField(responseField, value, i);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("PLEXSVC Failed to execute " + provider
                    + " with input " + requestFields, e);
        }
    }

    private void addIntermediateAndOptionalFields(
            final DataFieldRowSet requestFields,
            DataFieldRowSet optionalRequestFields,
            final DataFieldRowSet responseFields,
            Collection<DataProvider> providers) {
        // add any intermediate data needed to the output
        for (final DataProvider provider : providers) {
            for (MetaField metaField : provider.getMandatoryRequestFields()
                    .getMetaFields()) {
                if (!responseFields.getMetaFields().contains(metaField)) {
                    responseFields.getMetaFields().addMetaField(metaField);
                }
            }
            for (MetaField metaField : provider.getOptionalRequestFields()
                    .getMetaFields()) {
                optionalRequestFields.getMetaFields().addMetaField(metaField);
            }
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
