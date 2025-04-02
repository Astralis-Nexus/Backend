package application;

import application.config.ApplicationConfig;
import persistence.config.HibernateConfig;
import route.Route;

public class Application {
    public static void main(String[] args) {
        Route route = new Route(HibernateConfig.getEntityManagerFactoryConfig(false));

        ApplicationConfig app = ApplicationConfig.getInstance();
        app.initiateServer()
                .startServer(7007)
                .setExceptionHandlers()
                .setRoute(route.addRoutes());
    }
}
