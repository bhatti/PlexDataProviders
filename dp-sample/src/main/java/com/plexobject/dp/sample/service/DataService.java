package com.plexobject.dp.sample.service;

import java.util.Collection;

import javax.jws.WebService;

import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.handler.Request;

@WebService
public interface DataService {
    DataResponse query(Request request);

    Collection<DataProvider> info();
}