package com.plexobject.dp.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataFieldRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.provider.TaskGranularity;

public class ProvidersExecutor {
    private static final Logger logger = Logger
            .getLogger(ProvidersExecutor.class);
    private final DataFieldRowSet requestFields;
    private final DataFieldRowSet responseFields;
    private final DataConfiguration config;
    private final Collection<DataProvider> providers;
    private final ExecutorService executor;
    private final DataFieldRowSet optionalRequestFields = new DataFieldRowSet(
            new Metadata());
    private final Map<DataProvider, Throwable> providerErrors = new HashMap<>();

    ProvidersExecutor(final DataFieldRowSet requestFields,
            final DataFieldRowSet responseFields,
            final DataConfiguration config,
            final Collection<DataProvider> providers,
            final ExecutorService executor) {
        this.requestFields = requestFields;
        this.responseFields = responseFields;
        this.config = config;
        this.providers = providers;
        this.executor = executor;
    }

    public Map<DataProvider, Throwable> execute() {
        // Add intermediate data needed, e.g. we could call provider A that
        // returns some fields, which are used as input for provider B
        addIntermediateAndOptionalMetaFields();
        if (config.getQueryTimeoutMillis() > 0) {
            Future<?> future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    doExecute();
                }
            });
            // wait for main task to be completed
            // this may throw TimeoutException or ExecutionException
            waitForMainExecutorThread(future);
        } else {
            doExecute();
        }
        return providerErrors;
    }

    // go through all providers and retrieve response data fields
    // note that some of providers may not have all request data fields so we
    // will wait until we have the request fields
    private void doExecute() {
        // keep going until we are done with all providers
        while (providers.size() > 0) {
            // Execute providers that have the request fields available
            Collection<DataProvider> executedProviders = executeReadyProviders();
            if (providers.size() > 0 && executedProviders.size() == 0) {
                throw new IllegalStateException("Providers " + providers
                        + " cannot be fulfilled\n\trequestFields "
                        + requestFields + "\n\tresponseFields" + responseFields);
            }
            //
            // abort if configuration is set to abort upon partial failure
            if (config.isAbortUponPartialFailure() && providerErrors.size() > 0) {
                break;
            }
        }
    }

    // Go through all providers and if we have all the necessary request
    // parameters then we will execute it.
    private Collection<DataProvider> executeReadyProviders() {
        final Collection<DataProvider> executedProviders = new ArrayList<>();
        final Map<DataProvider, Future<?>> futures = new HashMap<>();
        for (final DataProvider provider : providers) {
            if (requestFields.getMetadata().getMissingCount(
                    provider.getMandatoryRequestFields()) == 0) {
                executedProviders.add(provider);
                // execute in background thread if provider will take long time
                // to execute
                if (provider.getTaskGranularity() == TaskGranularity.COARSE) {
                    Future<?> future = executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            executeProvider(provider);
                        }
                    });
                    futures.put(provider, future);
                } else {
                    // execute in the same thread
                    executeProvider(provider);
                }
            }
        }
        // remove all providers that have been executed from initial provider
        // list
        providers.removeAll(executedProviders);
        waitForExecutingProviders(futures);
        return executedProviders;
    }

    private void executeProvider(final DataProvider provider) {
        try {
            // calling data provider
            provider.produce(requestFields, responseFields, config);
            // adding intermediate data fields
            addRequestDataForIntermediateFields(provider);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to execute " + provider + " with input "
                        + requestFields, e);
            }
            addError(provider, e);
        }
    }

    // add intermediate fields needed for future providers
    // add optional fields as request parameters as well
    private void addRequestDataForIntermediateFields(final DataProvider provider) {
        for (int i = 0; i < responseFields.size(); i++) {
            for (MetaField responseField : provider.getResponseFields()
                    .getMetaFields()) {
                if ((responseFields.getMetadata().contains(responseField) && responseFields
                        .hasFieldValue(responseField, i))
                        || (optionalRequestFields.getMetadata().contains(
                                responseField) && responseFields.hasFieldValue(
                                responseField, i))) {
                    Object value = responseFields.getFieldValue(responseField,
                            i);
                    requestFields.addDataField(responseField, value, i);
                }
            }
        }
    }

    // add any intermediate meta data needed to the output
    private void addIntermediateAndOptionalMetaFields() {
        for (final DataProvider provider : providers) {
            for (MetaField metaField : provider.getMandatoryRequestFields()
                    .getMetaFields()) {
                if (!responseFields.getMetadata().contains(metaField)) {
                    responseFields.getMetadata().addMetaField(metaField);
                }
            }
            for (MetaField metaField : provider.getOptionalRequestFields()
                    .getMetaFields()) {
                optionalRequestFields.getMetadata().addMetaField(metaField);
                if (!responseFields.getMetadata().contains(metaField)) {
                    responseFields.getMetadata().addMetaField(metaField);
                }
            }
        }
    }

    // waiting for providers to finish
    private void waitForExecutingProviders(
            final Map<DataProvider, Future<?>> futures) {
        for (Map.Entry<DataProvider, Future<?>> e : futures.entrySet()) {
            while (!e.getValue().isDone() && !e.getValue().isCancelled()) {
                try {
                    e.getValue().get();
                } catch (InterruptedException ex) {
                    Thread.interrupted();
                } catch (Exception ex) {
                    addError(e.getKey(), ex);
                }
            }
        }
    }

    private void waitForMainExecutorThread(Future<?> future) {
        while (!future.isDone() && !future.isCancelled()) {
            try {
                future.get(config.getQueryTimeoutMillis(),
                        TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.interrupted();
            } catch (Exception e) {
                throw new DataProviderException("Failed to execute providers",
                        e);
            }
        }
    }

    private void addError(DataProvider provider, Throwable cause) {
        providerErrors.put(provider, cause);
    }
}
