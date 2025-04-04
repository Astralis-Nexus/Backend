package application;

import application.config.ApplicationConfig;
import data.PopulateData;
import jakarta.persistence.EntityManagerFactory;
import persistence.config.HibernateConfig;
import route.Route;

public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(false);

        Route route = new Route(emf);
        // Uncomment if data needed for the database
        //PopulateData.populateData(emf);

        ApplicationConfig app = ApplicationConfig.getInstance();
        app.initiateServer()
                .startServer(7007)
                .setExceptionHandlers()
                .setRoute(route.addRoutes());
    }
}
