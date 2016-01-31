package com.plexobject.dp.sample.dao;

import com.plexobject.dp.sample.dao.memory.AccountDaoImpl;
import com.plexobject.dp.sample.dao.memory.CompanyDaoImpl;
import com.plexobject.dp.sample.dao.memory.OrderDaoImpl;
import com.plexobject.dp.sample.dao.memory.PositionDaoImpl;
import com.plexobject.dp.sample.dao.memory.PositionGroupDaoImpl;
import com.plexobject.dp.sample.dao.memory.QuoteDaoImpl;
import com.plexobject.dp.sample.dao.memory.SecurityDaoImpl;
import com.plexobject.dp.sample.dao.memory.UserDaoImpl;
import com.plexobject.dp.sample.dao.memory.WatchlistDaoImpl;

public class DaoLocator {
    public static AccountDao accountDao = new AccountDaoImpl();
    public static CompanyDao companyDao = new CompanyDaoImpl();
    public static OrderDao orderDao = new OrderDaoImpl();
    public static PositionDao positionDao = new PositionDaoImpl();
    public static PositionGroupDao positionGroupDao = new PositionGroupDaoImpl();
    public static QuoteDao quoteDao = new QuoteDaoImpl();
    public static SecurityDao securityDao = new SecurityDaoImpl();
    public static UserDao userDao = new UserDaoImpl();
    public static WatchlistDao watchlistDao = new WatchlistDaoImpl();

}
