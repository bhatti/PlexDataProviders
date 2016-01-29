package com.plexobject.dp.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public Map<DataProvider, Throwable> produce(DataFieldRowSet requestFields,
            DataFieldRowSet responseFields, DataConfiguration config) {
        // Get all data providers needed
        Collection<DataProvider> providers = getDataProviders(
                requestFields.getMetaFields(), responseFields.getMetaFields());
        if (logger.isDebugEnabled()) {
            logger.debug("Executing requestFields " + requestFields
                    + ", responseFields " + responseFields + " with "
                    + providers);
        }
        //
        // creating parallel thread executor
        final ExecutorService executor = defaultExecutor != null ? defaultExecutor
                : Executors.newFixedThreadPool(getThreadPoolSize(providers));
        try {
            return new ProvidersExecutor(requestFields, responseFields, config,
                    providers, executor).execute();
        } finally {
            if (executor != defaultExecutor) {
                executor.shutdown();
            }
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
                        + responseField);
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

    // This method finds data provider that matches or almost matches the
    // request parameters that we have
    private DataProvider getBestDataProvider(Set<DataProvider> providers,
            MetaFields requestFields) {
        DataProvider bestProvider = null;
        int minCount = Integer.MAX_VALUE;
        int maxRank = Integer.MIN_VALUE;
        for (DataProvider provider : providers) {
            int count = provider.getMandatoryRequestFields().getMissingCount(
                    requestFields);
            if (count < minCount) {
                minCount = count;
                maxRank = provider.getRank();
                bestProvider = provider;
            } else if (count == minCount && provider.getRank() > maxRank) {
                maxRank = provider.getRank();
                bestProvider = provider;
            }
        }
        return bestProvider;
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
