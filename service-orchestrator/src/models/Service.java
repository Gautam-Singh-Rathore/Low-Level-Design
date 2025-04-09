package models;

import algorithms.Router;

public class Service { // Different microservices like login-service , payment-service etc.
    //Bundles together: a Router (strategy) + Service ID + Methods.
    private final Router router; // Each service can use a different load balancing technique
    // Each service has its own Router(load balancing strategy)

    private final String id;
    private final String[] methods;

    public Service(Router router, String id, String[] methods) {
        this.router = router;
        this.id = id;
        this.methods = methods;
    }

    public Router getRouter() {
        return router;
    }

    public String getId() {
        return id;
    }

    public String[] getMethods() {
        return methods;
    }
}
