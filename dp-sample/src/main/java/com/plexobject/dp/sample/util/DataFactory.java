package com.plexobject.dp.sample.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.AccountType;
import com.plexobject.dp.sample.domain.Address;
import com.plexobject.dp.sample.domain.Beta;
import com.plexobject.dp.sample.domain.Company;
import com.plexobject.dp.sample.domain.CompanyEvent;
import com.plexobject.dp.sample.domain.Equity;
import com.plexobject.dp.sample.domain.MarketSession;
import com.plexobject.dp.sample.domain.Option;
import com.plexobject.dp.sample.domain.OptionRoot;
import com.plexobject.dp.sample.domain.OptionType;
import com.plexobject.dp.sample.domain.Order;
import com.plexobject.dp.sample.domain.OrderLeg;
import com.plexobject.dp.sample.domain.OrderSide;
import com.plexobject.dp.sample.domain.OrderStatus;
import com.plexobject.dp.sample.domain.Permission;
import com.plexobject.dp.sample.domain.Portfolio;
import com.plexobject.dp.sample.domain.Position;
import com.plexobject.dp.sample.domain.PositionGroup;
import com.plexobject.dp.sample.domain.PositionType;
import com.plexobject.dp.sample.domain.PriceType;
import com.plexobject.dp.sample.domain.Quote;
import com.plexobject.dp.sample.domain.Role;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.domain.SecurityType;
import com.plexobject.dp.sample.domain.TimeOfSales;
import com.plexobject.dp.sample.domain.Trade;
import com.plexobject.dp.sample.domain.User;
import com.plexobject.dp.sample.domain.Watchlist;

public class DataFactory {
    private static AtomicLong nextId = new AtomicLong();
    private static String[] EQUITY_SYMBOLS = { "AAPL", "GOOG", "M", "F", "C",
            "N", "A", "B", "D", "E", "G" };
    private static String[] OPTION_SYMBOLS = { "AAPL_X", "GOOG_X", "M_X",
            "F_X", "C_X", "N_X", "A_X", "B_X", "D_X", "E_X", "G_X" };
    private static String[] EXCHANGES = { "NYSE", "NASDAQ", "DOW" };
    private static String[] OPRAS = { "A", "B", "C", "D" };

    public static void clearData() {
        DaoLocator.accountDao.clear();
        DaoLocator.companyDao.clear();
        DaoLocator.orderDao.clear();
        DaoLocator.positionDao.clear();
        DaoLocator.positionGroupDao.clear();
        DaoLocator.quoteDao.clear();
        DaoLocator.securityDao.clear();
        DaoLocator.userDao.clear();
        DaoLocator.watchlistDao.clear();
    }

    public static void addData() {
        for (int i = 0; i < 10; i++) {
            Company company = createCompany();
            DaoLocator.companyDao.save(company);
        }
        for (int i = 0; i < 10; i++) {
            User user = createUser();
            DaoLocator.userDao.save(user);
            for (Account account : user.getAccounts()) {
                DaoLocator.accountDao.save(account);
                for (int j = 0; j < 20; j++) {
                    Order order = createOrder(account);
                    DaoLocator.orderDao.save(order);
                    DaoLocator.quoteDao.save(createQuote(order.getSecurity()));
                    DaoLocator.securityDao.save(order.getSecurity());
                }
            }
            for (Watchlist watchlist : user.getWatchlists()) {
                DaoLocator.watchlistDao.save(watchlist);
                for (Security security : watchlist.getSecurities()) {
                    DaoLocator.securityDao.save(security);
                }
            }
        }
    }

    public static Quote createQuote() {
        long id = nextId.incrementAndGet();
        return createQuote(id % 2 == 0 ? createEquity() : createOption());
    }

    public static Quote createQuote(Security security) {
        long id = nextId.incrementAndGet();
        Quote quote = new Quote();
        quote.setSecurity(security);
        quote.setBidPrice(rand(10, 100));
        quote.setAskPrice(rand(10, 100));
        quote.setClosePrice(rand(10, 100));
        quote.setTradePrice(rand(10, 100));
        quote.setMarkPrice(rand(10, 100));
        quote.setVolume(rand(1000, 5000));
        quote.setMarketSession(MarketSession.values()[(int) id
                % MarketSession.values().length]);

        for (int i = 0; i < 5; i++) {
            quote.getSales().add(createTimeOfSales(security));
        }
        return quote;
    }

    public static TimeOfSales createTimeOfSales(Security security) {
        long id = nextId.incrementAndGet();
        TimeOfSales sales = new TimeOfSales();
        sales.setSymbol(security.getSymbol());
        sales.setDate(new Date());
        sales.setPrice(rand(1, 100));
        sales.setVolume(rand(1, 100));
        sales.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);

        return sales;
    }

    public static Position createPosition(Order parentOrder) {
        long id = nextId.incrementAndGet();
        Position position = new Position();
        position.setId(id);
        position.setAccount(parentOrder.getAccount());
        position.setQuantity(parentOrder.getFillQuantity());
        position.setPrice(parentOrder.getFillPrice());
        position.setSecurity(parentOrder.getSecurity());
        PositionGroup group = parentOrder.getAccount().getPositionGroups()
                .get(parentOrder.getSecurity().getSymbol());
        if (group == null) {
            group = new PositionGroup();
            group.setId(nextId.incrementAndGet());
            group.setName(parentOrder.getSecurity().getSymbol());
            group.getPositions().add(position);
            parentOrder.getAccount().getPositionGroups()
                    .put(parentOrder.getSecurity().getSymbol(), group);
        }
        return position;
    }

    public static Order createOrder(Account account) {
        long id = nextId.incrementAndGet();
        Order order = new Order();
        order.setId(id);
        order.setDate(new Date());
        order.setAccount(account);
        order.setMarketSession(MarketSession.values()[(int) id
                % MarketSession.values().length]);
        order.setStatus(OrderStatus.values()[(int) id
                % OrderStatus.values().length]);
        order.setPriceType(id % 2 == 0 ? PriceType.LIMIT : PriceType.MARKET);
        order.setSecurity(id % 2 == 0 ? createEquity() : createOption());
        order.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);
        order.setPrice(rand(10, 100));
        order.setQuantity(rand(10, 100));
        if (order.getStatus() == OrderStatus.FILLED) {
            order.setFillDate(new Date());
            order.setFillPrice(order.getPrice());
            order.setFillQuantity(order.getQuantity());
            Position position = createPosition(order);
            account.getPositions().add(position);

        }
        for (int i = 0; i < 5; i++) {
            order.getOrderLegs().add(createOrderLeg(order));
        }
        account.getOrders().add(order);
        return order;
    }

    public static OrderLeg createOrderLeg(Order parentOrder) {
        long id = nextId.incrementAndGet();
        OrderLeg orderLeg = new OrderLeg();
        orderLeg.setId(id);
        orderLeg.setSide(id % 2 == 0 ? OrderSide.BUY : OrderSide.SELL);
        orderLeg.setPrice(rand(10, 100));
        orderLeg.setQuantity(rand(10, 100));
        if (parentOrder.getStatus() == OrderStatus.FILLED) {
            orderLeg.setFillPrice(orderLeg.getPrice());
            orderLeg.setFillQuantity(orderLeg.getQuantity());
        }
        orderLeg.setPositionType(id % 2 == 0 ? PositionType.CASH
                : PositionType.MARGIN);
        for (int i = 0; i < 3; i++) {
            orderLeg.getTrades().add(createTrade(parentOrder.getSecurity()));
        }
        return orderLeg;
    }

    public static Trade createTrade(Security security) {
        long id = nextId.incrementAndGet();
        Trade trade = new Trade();
        trade.setId(id);
        trade.setQuantity(rand(1, 100));
        trade.setPrice(rand(1, 100));
        trade.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);
        trade.setDate(new Date());
        trade.setSecurity(security);
        return trade;
    }

    public static Company createCompany() {
        long id = nextId.incrementAndGet();
        Company company = new Company();
        company.setSymbol(EQUITY_SYMBOLS[(int) id % EQUITY_SYMBOLS.length]);
        company.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);
        company.setName(company.getSymbol() + " - name");
        company.setAddress(createAddress());
        for (int i = 0; i < 5; i++) {
            company.getEvents().add(createCompanyEvent());
        }
        return company;
    }

    public static User createUser() {
        long id = nextId.incrementAndGet();
        User user = new User();
        user.setId(id);
        user.setUsername("user" + id);
        user.setName("name" + id);
        user.setEmail("email" + id + "@testing.tt");
        user.setAddress(createAddress());
        user.setDateOfBirth(new Date());
        for (int i = 0; i < 5; i++) {
            user.getAccounts().add(createAccount());
        }
        for (int i = 0; i < 5; i++) {
            user.getRoles().add(createRole());
        }
        for (int i = 0; i < 5; i++) {
            user.getWatchlists().add(createWatchlist());
        }
        user.setPortfolio(createPortfolio());
        return user;
    }

    public static Portfolio createPortfolio() {
        long id = nextId.incrementAndGet();
        Portfolio portfolio = new Portfolio();
        portfolio.setId(id);
        portfolio.setCash(rand(1000, 5000));
        portfolio.setMargin(rand(1000, 5000));
        return portfolio;
    }

    public static Permission createPermission() {
        long id = nextId.incrementAndGet();
        Permission permission = new Permission();
        permission.setId(id);
        permission.setResource("resource" + id);
        permission.setActions(new String[] { "read", "write" });
        return permission;
    }

    public static Role createRole() {
        long id = nextId.incrementAndGet();
        Role role = new Role();
        role.setId(id);
        role.setName("role " + id);
        for (int i = 0; i < 5; i++) {
            role.getPermissions().add(createPermission());
        }
        return role;
    }

    public static Address createAddress() {
        long id = nextId.incrementAndGet();
        Address address = new Address();
        address.setId(id);
        address.setStreet("street " + id);
        address.setCity("city " + id);
        address.setState("state " + id);
        address.setZip("zip " + id);
        address.setCountry("country " + id);
        return address;
    }

    public static Account createAccount() {
        long id = nextId.incrementAndGet();
        Account account = new Account();
        account.setId(id);
        account.setAccountName("account " + id);
        account.setAccountType(id % 2 == 0 ? AccountType.CASH
                : AccountType.MARGIN);

        return account;
    }

    public static Beta createBeta() {
        long id = nextId.incrementAndGet();
        Beta beta = new Beta();
        beta.setBeta(rand(1, 5));
        beta.setMonth((int) id % 11);
        return beta;
    }

    public static Watchlist createWatchlist() {
        long id = nextId.incrementAndGet();
        Watchlist watchlist = new Watchlist();
        watchlist.setId(id);
        watchlist.setName("name " + id);
        for (int i = 0; i < 3; i++) {
            watchlist.getSecurities().add(createEquity());
        }
        for (int i = 0; i < 2; i++) {
            watchlist.getSecurities().add(createOption());
        }
        return watchlist;
    }

    public static Equity createEquity() {
        long id = nextId.incrementAndGet();
        return createEquity(EQUITY_SYMBOLS[(int) id % EQUITY_SYMBOLS.length]);
    }

    public static Equity createEquity(String symbol) {
        long id = nextId.incrementAndGet();
        Equity equity = new Equity();
        equity.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);
        equity.setSymbol(symbol);
        equity.setUnderlyingSymbol(equity.getSymbol());
        equity.setType(SecurityType.STOCK);
        equity.setBeta(createBeta());
        equity.setDividendRate(rand(1, 50));
        return equity;
    }

    public static Option createOption() {
        long id = nextId.incrementAndGet();
        Option option = new Option();
        option.setExchange(EXCHANGES[(int) id % EXCHANGES.length]);
        int symbolIndex = (int) id % OPTION_SYMBOLS.length;
        option.setSymbol(OPTION_SYMBOLS[symbolIndex]);
        option.setUnderlyingSymbol(EQUITY_SYMBOLS[symbolIndex]);
        option.setType(SecurityType.OPTION);
        option.setBeta(createBeta());
        option.setOptionRoot(createOptionRoot(option.getUnderlyingSymbol()));
        option.setOptionType(id % 2 == 0 ? OptionType.CALL : OptionType.PUT);
        option.setStrikePrice(rand(10, 100));
        option.setExpirationDate(new Date());
        return option;
    }

    public static OptionRoot createOptionRoot(String symbol) {
        long id = nextId.incrementAndGet();
        OptionRoot optionRoot = new OptionRoot();
        optionRoot.setOpraRoot(OPRAS[(int) id % OPRAS.length]);
        optionRoot.setUnderlying(createEquity(symbol));
        optionRoot.setMultiplier(BigDecimal.valueOf(100));
        optionRoot.setExcerciseStyle("American");
        return optionRoot;
    }

    public static CompanyEvent createCompanyEvent() {
        long id = nextId.incrementAndGet();
        CompanyEvent companyEvent = new CompanyEvent();
        companyEvent.setId(id);
        companyEvent.setName("name " + id);
        companyEvent.setDate(new Date());
        companyEvent.setType(id % 2 == 0 ? CompanyEvent.Type.DIVIDEND
                : CompanyEvent.Type.EARNINGS);
        return companyEvent;
    }

    private static BigDecimal rand(double min, double max) {
        double num = Math.abs(min + (Math.random() * ((max - min) + 1)));
        return new BigDecimal(Double.toString(num));
    }
}
