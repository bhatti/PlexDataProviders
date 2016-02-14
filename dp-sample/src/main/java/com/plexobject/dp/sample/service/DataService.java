package com.plexobject.dp.sample.service;

import javax.jws.WebService;
import javax.ws.rs.QueryParam;

import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.sample.domain.DataInfoResponse;
import com.plexobject.handler.Request;

@WebService
public interface DataService {
    DataResponse query(Request request);

    DataInfoResponse info(@QueryParam("kind") String kind);
}
