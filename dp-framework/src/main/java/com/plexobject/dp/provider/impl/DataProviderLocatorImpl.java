package com.plexobject.dp.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.metrics.Metric;
import com.plexobject.dp.metrics.Timer;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.provider.DataProviderLocator;

public class DataProviderLocatorImpl implements DataProviderLocator {
    private static final Logger logger = Logger
            .getLogger(DataProviderLocatorImpl.class);

    private ConcurrentHashMap<MetaField, Set<DataProvider>> providersByOutputMetaField = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, DataProvider> providersByName = new ConcurrentHashMap<>();

    @Override
    public void register(DataProvider provider) {
        synchronized (providersByName) {
            if (providersByName.get(provider.getName()) != null) {
                throw new IllegalArgumentException("Provider with name "
                        + provider.getName() + " is already registered");
            }
            providersByName.put(provider.getName(), provider);
        }
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
        synchronized (providersByName) {
            providersByName.remove(provider.getName());
        }
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
    @Override
    public Collection<DataProvider> locate(Metadata requestFields,
            Metadata responseFields) {
        final Timer timer = Metric.newTimer("DataProviderLocatorImpl.locate");
        try {
            final List<DataProvider> providers = new ArrayList<>();
            populateDataProviders(new Metadata(requestFields.getMetaFields()),
                    new Metadata(responseFields.getMetaFields()), providers);
            Collections.sort(providers); // sort by dependency
            if (logger.isDebugEnabled()) {
                logger.info("locate selected " + providers + " for input "
                        + requestFields + ", output " + responseFields);
            }
            return providers;
        } finally {
            timer.stop();
        }
    }

    @Override
    public Collection<DataProvider> getAll() {
        Set<DataProvider> providers = new HashSet<DataProvider>();
        for (Set<DataProvider> set : providersByOutputMetaField.values()) {
            providers.addAll(set);
        }
        return providers;
    }

    @Override
    public DataProvider getByName(String name) {
        return providersByName.get(name);
    }

    private void populateDataProviders(final Metadata requestFields,
            final Metadata responseFields,
            final List<DataProvider> existingProviders) {
        responseFields.removeMetadata(requestFields);
        //

        for (MetaField responseField : responseFields.getMetaFields()) {
            // check if responseField is available from existing providers
            boolean matchedExistingProviders = false;
            for (DataProvider provider : existingProviders) {
                if (provider.getResponseFields().contains(responseField)) {
                    matchedExistingProviders = true;
                    break;
                }
            }
            if (matchedExistingProviders) {
                continue;
            }
            // Otherwise find providers that have the field in the response list
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
            Metadata missingFields = requestFields.getMissingMetadata(provider
                    .getMandatoryRequestFields());

            // add output fields to requests so that we can use it for other
            // providers
            requestFields.addMetadata(provider.getResponseFields());
            //
            if (missingFields.size() > 0) {
                populateDataProviders(requestFields, missingFields,
                        existingProviders);
            }
        }
    }

    // This method finds data provider that matches or almost matches the
    // request parameters that we have
    private DataProvider getBestDataProvider(
            Collection<DataProvider> providers, Metadata requestFields) {
        DataProvider bestProvider = null;
        int bestMinCount = Integer.MAX_VALUE;
        int bestMatchingCount = Integer.MIN_VALUE;
        int bestRank = Integer.MIN_VALUE;
        int bestRequiredFields = Integer.MAX_VALUE;

        for (DataProvider provider : providers) {
            int missingCount = provider.getMandatoryRequestFields()
                    .getMissingCount(requestFields);
            int matchingCount = provider.getMandatoryRequestFields()
                    .getMatchingCount(requestFields);
            // The best provider will the provider that has least number of
            // missing required fields from the request fields provided,
            // otherwise we would use the rank or the provider with least number
            // of required fields or most number of matching fields with input
            if (missingCount < bestMinCount
                    || (missingCount == bestMinCount && provider.getRank() > bestRank)
                    || (missingCount == bestMinCount && provider
                            .getMandatoryRequestFields().size() < bestRequiredFields)
                    || (missingCount == bestMinCount && matchingCount > bestMatchingCount)
                    || (missingCount == bestMinCount && provider
                            .getMandatoryRequestFields().size() < bestRequiredFields)) {
                bestRequiredFields = provider.getMandatoryRequestFields()
                        .size();
                bestMinCount = missingCount;
                bestMatchingCount = matchingCount;
                bestRank = provider.getRank();
                bestProvider = provider;
            }
        }
        return bestProvider;
    }

}
