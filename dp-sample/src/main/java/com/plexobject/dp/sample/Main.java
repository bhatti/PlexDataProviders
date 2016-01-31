package com.plexobject.dp.sample;

import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.plexobject.domain.Configuration;
import com.plexobject.dp.sample.util.DataFactory;
import com.plexobject.handler.RequestHandler;
import com.plexobject.handler.ws.WSRequestHandlerAdapter;
import com.plexobject.service.ServiceConfigDesc;
import com.plexobject.service.ServiceRegistry;
import com.plexobject.service.ServiceRegistryLifecycleAware;

public class Main implements ServiceRegistryLifecycleAware {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java " + Main.class.getName()
                    + " properties-file");
            System.exit(1);
        }
        //
        DataFactory.addData();
        //
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.INFO);
        Configuration config = new Configuration(args[0]);

        ServiceRegistry serviceRegistry = new ServiceRegistry(config);
        WSRequestHandlerAdapter requestHandlerAdapter = new WSRequestHandlerAdapter(
                serviceRegistry);
        Map<ServiceConfigDesc, RequestHandler> handlers = requestHandlerAdapter
                .createFromPackages("com.plexobject.dp.sample.service");
        for (Map.Entry<ServiceConfigDesc, RequestHandler> e : handlers
                .entrySet()) {
            serviceRegistry.addRequestHandler(e.getKey(), e.getValue());
        }

        serviceRegistry.start();
        Thread.currentThread().join();
    }

    @Override
    public void onStarted(ServiceRegistry serviceRegistry) {
    }

    @Override
    public void onStopped(ServiceRegistry serviceRegistry) {
    }
}
