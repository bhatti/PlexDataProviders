package com.plexobject.dp.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.provider.DataProviderLocator;

public class DataProviderLocatorImpl implements DataProviderLocator {
    private ConcurrentHashMap<MetaField, Set<DataProvider>> providersByOutputMetaField = new ConcurrentHashMap<>();

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
    @Override
    public Collection<DataProvider> locate(Metadata requestFields,
            Metadata responseFields) {
        final List<DataProvider> providers = new ArrayList<>();
        populateDataProviders(new Metadata(requestFields.getMetaFields()),
                new Metadata(responseFields.getMetaFields()), providers);
        Collections.sort(providers); // sort by dependency
        return providers;
    }

    private void populateDataProviders(Metadata requestFields,
            Metadata responseFields, List<DataProvider> existingProviders) {
        responseFields.removeMetadata(requestFields);
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
    private DataProvider getBestDataProvider(Set<DataProvider> providers,
            Metadata requestFields) {
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
}
