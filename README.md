# PlexDataProviders
Java framework for managing general-purpose data providers and data query.

##Overview

PlexDataProviders is a light-weight Java framework that abstract access to various data providers such as databases, files, web services, etc. It allows aggregation of data from various data providers. 

The PlexDataProviders framework is divided into two components:
- Data Provider - This component defines interfaces that are implemented to access data sources such as database or web services.
- Query Engine - This component is used for querying and aggregating data.

The query engine can determine dependency between providers and it also allow you to use output of one of the data provider as input to another data provider. For example, let's assume:
- data-provider A requires input-a1, input-a2 and produces output-a1, output-a2
- data-provider B requires input-b1 and output-a1 and produces output-b1, output-b2

Then you can pass input-a1, input-a2 to the query engine and request output-a1, output-a2, output-b1, output-b2 output data fields.


##Benefits
PlexDataProviders provides offers following benefits:
- It provides a unified way to search data and abstracts integration to underlying data sources. 
- It helps simplifying client side logic as they can use a single data service to query all data instead of using multiple data services. 
- This also help with managing end-points as you only a single end-point instead of connecting to multiple web services.
- As clients can specify the data they need, this helps with payload size and network bandwidth. 
- The clients only need to create a single data parser so it keeps JSON parsing logic simple. 
- As PlexDataProviders supports multi-threading, it also helps with latency of the data fetch requests.
- It partial failure so that a failure in a single data provider doesn't effect other data providers and the data service can still return partial results. User 
- It supports timeout so that clients can receive available data that completes in given timeout interval.

##Building
- Download and install <a href="http://www.gradle.org/downloads">Gradle</a>.
- Download and install <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">Java 8</a>.
- Checkout code using 

```
git clone git@github.com:bhatti/PlexDataProviders.git
```

- Compile and build jar file using

```
gradle jar
```

- Copy and add jar file (build/libs/DataProviders-0.1-SNAPSHOT.jar) manually in your application.



##Version
- 0.1

##License
- MIT

## Data Structure

Following are primary data structures:
- MetaField - This class defines meta information for each data field such as name, kind, type, etc.

- MetaFieldType - This enum class supports primitive data types supported, i.e.
  - SCALAR_TEXT - simple text
  - SCALAR_INTEGER - integer numbers
  - SCALAR_DECIMAL - decimal numbers
  - SCALAR_DATE - dates
  - SCALAR_BOOLEAN - boolean 
  - VECTOR_TEXT - array of text
  - VECTOR_INTEGER - array of integers
  - VECTOR_DECIMAL - array of decimals
  - VECTOR_DATE - array of dates
  - VECTOR_BOOLEAN - array of boolean
  - BINARY - binary data
  - ROWSET - nested data rowsets

- Metadata - This class defines a set of MetaFields used in DataRow/DataRowSet 

- DataRow - This class abstracts a row of data fields 

- DataRowSet - This class abstracts a set of rows 

PlexDataProviders also supports nested structures where a data field in DataRow can be instance of DataRowSet.

### Adding a Data Provider 
The data provider implements following two interfaces
```java 
public interface DataProducer {
    void produce(DataRowSet requestFields, DataRowSet responseFields,
            QueryConfiguration config) throws DataProviderException;
}

```

Note that QueryConfiguration defines additional parameters such as:
- pagination parameters
- ordering/grouping
- filtering parameters
- timeout parameters

The timeout parameter can be used to return all available data within defined time, e.g. query engine may invoke underlying data providers in multiple threads and if underlying query takes a long time then it would return available data.


```java 
public interface DataProvider extends DataProducer, Comparable<DataProvider> {
    String getName();

    int getRank();

    Metadata getMandatoryRequestMetadata();

    Metadata getOptionalRequestMetadata();

    Metadata getResponseMetadata();

    TaskGranularity getTaskGranularity();
}
```
Each provider defines name, rank (or priority when matching for best provider), set of mandatory/optional input and output data fields. The data provider can also define granularity as coarse grain or fine grain and the implementation may execute those providers on different threads.

PlexDataProviders also provides interfaces for converting data from domain objects to DataRowSet.  Here is an example of provider implementation:
```java 
public class SecuritiesBySymbolsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static SecurityMarshaller marshaller = new SecurityMarshaller();

    public SecuritiesBySymbolsProvider() {
        super("SecuritiesBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            QueryConfiguration config) throws DataProviderException {
        final String id = parameter.getValueAsText(SharedMeta.symbol, 0);
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("symbol", id.toUpperCase());
        Collection<Security> securities = DaoLocator.securityDao.query(criteria);
        DataRowSet rowset = marshaller.marshal(securities);
        addRowSet(response, rowset, 0);
    }
}
```

Typically, you will create data-provider for each different kind of query that you want to support. Each data provider specifies set of required and optional data fields that can be used to generate output data fields. 

Here is an example of marshalling data from Securty domain objects to DataRowSet:

```java 
public DataRowSet marshal(Security security) {
    DataRowSet rowset = new DataRowSet(responseMeta);
    marshal(rowset, security, 0);
    return rowset;
}

public DataRowSet marshal(Collection<Security> securities) {
    DataRowSet rowset = new DataRowSet(responseMeta);
    for (Security security : securities) {
        marshal(rowset, security, rowset.size());
    }
    return rowset;
}
...
```


PlexDataProviders provides DataProviderLocator interface for registering and looking up provider, e.g. 
```java 
public interface DataProviderLocator {
    void register(DataProvider provider);

    Collection<DataProvider> locate(Metadata requestMetadata, Metadata responseMetadata);
...
}
```

PlexDataProviders comes with a small application that provides data services by implementing various data providers. It uses PlexService framework for defining the service, e.g.
```java 
@WebService
@Path("/data")
public class DataServiceImpl implements DataService {
    private DataProviderLocator dataProviderLocator = new DataProviderLocatorImpl();
    private QueryEngine queryEngine = new QueryEngineImpl(dataProviderLocator);

    public DataServiceImpl() {
        dataProviderLocator.register(new AccountsByIdsProvider());
        dataProviderLocator.register(new AccountsByUseridProvider());
        dataProviderLocator.register(new CompaniesBySymbolsProvider());
        dataProviderLocator.register(new OrdersByAccountIdsProvider());
        dataProviderLocator.register(new PositionGroupsBySymbolsProvider());
        dataProviderLocator.register(new PositionsBySymbolsProvider());
        dataProviderLocator.register(new QuotesBySymbolsProvider());
        dataProviderLocator.register(new SecuritiesBySymbolsProvider());
        dataProviderLocator.register(new UsersByIdsProvider());
        dataProviderLocator.register(new WatchlistByUserProvider());
        dataProviderLocator.register(new SymbolsProvider());
        dataProviderLocator.register(new UsersProvider());
        dataProviderLocator.register(new SymbolSearchProvider());
    }

    @Override
    @GET
    public DataResponse query(Request webRequest) {
        final DataRequest dataRequest = DataRequest.from(webRequest .getProperties());
        return queryEngine.query(dataRequest);
    }
}

```

As you can see the data service simply builds DataRequest with input data fields and sends back response back to clients.

Here is an example client that passes a search query data field and requests quote data fields with company details
```java 
public void testGetQuoteBySearch() throws Throwable {
    String jsonResp = TestWebUtils.httpGet("http://localhost:" + DEFAULT_PORT
                    + "/data?fields=exchange,symbol,quote.bidPrice,quote.askPrice,quote.sales,company.name&symbolQuery=AAPL");
    ...
```

Note that above request will use three data providers, first it uses SymbolSearchProvider provider to search for matching symbols with given query. It then uses the symbol data field to request company and quote data fields from QuotesBySymbolsProvider and CompaniesBySymbolsProvider. The PlexDataProviders framework will take care of all dependency management for providers.


Here is an example JSON response from the data service:
```javascript 
{
    "queryResponse": {
        "fields": [
            [{
                "symbol": "AAPL_X"
            }, {
                "quote.sales": [
                    [{
                        "symbol": "AAPL_X"
                    }, {
                        "timeOfSale.volume": 56
                    }, {
                        "timeOfSale.exchange": "DOW"
                    }, {
                        "timeOfSale.date": 1455426008762
                    }, {
                        "timeOfSale.price": 69.49132317180353
                    }],
                    [{
                        "symbol": "AAPL_X"
                    }, {
                        "timeOfSale.volume": 54
                    }, {
                        "timeOfSale.exchange": "NYSE"
                    }, {
                        "timeOfSale.date": 1455426008762
                    }, {
                        "timeOfSale.price": 16.677774132458076
                    }],
                    [{
                        "symbol": "AAPL_X"
                    }, {
                        "timeOfSale.volume": 99
                    }, {
                        "timeOfSale.exchange": "NASDAQ"
                    }, {
                        "timeOfSale.date": 1455426008762
                    }, {
                        "timeOfSale.price": 42.17891320885568
                    }],
                    [{
                        "symbol": "AAPL_X"
                    }, {
                        "timeOfSale.volume": 49
                    }, {
                        "timeOfSale.exchange": "DOW"
                    }, {
                        "timeOfSale.date": 1455426008762
                    }, {
                        "timeOfSale.price": 69.61680149649729
                    }],
                    [{
                        "symbol": "AAPL_X"
                    }, {
                        "timeOfSale.volume": 69
                    }, {
                        "timeOfSale.exchange": "NYSE"
                    }, {
                        "timeOfSale.date": 1455426008762
                    }, {
                        "timeOfSale.price": 25.353316897552833
                    }]
                ]
            }, {
                "quote.askPrice": 54.99300665695502
            }, {
                "quote.bidPrice": 26.935682182171643
            }, {
                "exchange": "DOW"
            }, {
                "company.name": "AAPL - name"
            }],
            [{
                "symbol": "AAPL"
            }, {
                "exchange": "NASDAQ"
            }]
        ],
        "errorsByProviderName": {},
        "providers": ["QuotesBySymbolsProvider", "SymbolSearchProvider", "CompaniesBySymbolsProvider"]
    }
}
```

You can also retrieve nested relationships, e.g. following example returns all users along with their account and portfolio information:
```java 
public void testGetAccounts() throws Throwable {
            String jsonResp = TestWebUtils.httpGet("http://localhost:" + DEFAULT_PORT + 
            "/data?fields=userId,user.accounts,user.portfolio");
    ...
``` 
Following is a sample response from above request:

```javascript 
{
    "queryResponse": {
        "fields": [
            [{
                "user.portfolio": [
                    [{
                        "portfolioId": 448
                    }, {
                        "portfolio.margin": 2199.343269236506
                    }, {
                        "portfolio.cash": 2488.5370398814766
                    }]
                ]
            }, {
                "userId": 311
            }, {
                "user.accounts": [
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 313
                    }, {
                        "account.name": "account 313"
                    }],
                    [{
                        "account.type": "CASH"
                    }, {
                        "accountId": 314
                    }, {
                        "account.name": "account 314"
                    }],
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 315
                    }, {
                        "account.name": "account 315"
                    }],
                    [{
                        "account.type": "CASH"
                    }, {
                        "accountId": 316
                    }, {
                        "account.name": "account 316"
                    }],
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 317
                    }, {
                        "account.name": "account 317"
                    }]
                ]
            }],
            [{
                "user.portfolio": [
                    [{
                        "portfolioId": 3824
                    }, {
                        "portfolio.margin": 2172.6109918377097
                    }, {
                        "portfolio.cash": 1282.9727530033433
                    }]
                ]
            }, {
                "userId": 3687
            }, {
                "user.accounts": [
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 3689
                    }, {
                        "account.name": "account 3689"
                    }],
                    [{
                        "account.type": "CASH"
                    }, {
                        "accountId": 3690
                    }, {
                        "account.name": "account 3690"
                    }],
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 3691
                    }, {
                        "account.name": "account 3691"
                    }],
                    [{
                        "account.type": "CASH"
                    }, {
                        "accountId": 3692
                    }, {
                        "account.name": "account 3692"
                    }],
                    [{
                        "account.type": "MARGIN"
                    }, {
                        "accountId": 3693
                    }, {
                        "account.name": "account 3693"
                    }]
                ]
            }],
            [{
                "user.portfolio": [
                    [{
                        "portfolioId": 7200
                    }, {
                        "portfolio.margin": 2706.1256621272414
                    }, {
                        "portfolio.cash": 1764.2884398117876
                    }]
                ]
            }]
        ],
        "errorsByProviderName": {},
        "providers": ["UsersProvider", "UsersByIdsProvider"]
    }
}
```
Note that requesting composite data fields such as user.accounts and user.portfolio would return all nested data fields as well.

You can browse sample application for more examples.


## API Doc
[Java Doc](http://bhatti.github.io/PlexDataProviders/javadoc/)


## Sample Applications
      You can view sample applications under dp-sample folder for detailed examples of services and various configurations.

## Support or Contact
      Email bhatti AT plexobject DOT com for any questions or suggestions.

