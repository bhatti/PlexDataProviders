package com.plexobject.dp.sample.service;

import java.util.Collection;

import javax.jws.WebService;

import com.plexobject.dp.sample.domain.Order;

@WebService
public interface OrderService {
    Order create(Order order);

    Order get(long orderId);

    Collection<Order> findByAccounts(long accountId);
}