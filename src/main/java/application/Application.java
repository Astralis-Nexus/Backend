package application;

import application.config.ApplicationConfig;
import jakarta.persistence.EntityManagerFactory;
import persistence.config.HibernateConfig;
import route.Route;

@lombok.Generated
public class Application {

    public static void main(String[] args) {
        boolean isTest = true;
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(isTest);

        ApplicationConfig.getInstance()
                .initiateServer()
                .startServer(7007)
                .setRoute(new Route(emf).addRoutes());
    }
}
