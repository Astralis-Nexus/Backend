package route;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Route {
    AccountRoute accountRoute;
    FooterRoute footerRoute;
    GameRoute gameRoute;
    HeaderRoute headerRoute;
    InformationRoute informationRoute;
    LicenseRoute licenseRoute;
    QARoute qaRoute;
    RoleRoute roleRoute;
    TodoRoute todoRoute;

    public Route(EntityManagerFactory emf) {
        accountRoute = new AccountRoute(emf);
        footerRoute = new FooterRoute(emf);
        gameRoute = new GameRoute(emf);
        headerRoute = new HeaderRoute(emf);
        informationRoute = new InformationRoute(emf);
        licenseRoute = new LicenseRoute(emf);
        qaRoute = new QARoute(emf);
        roleRoute = new RoleRoute(emf);
        todoRoute = new TodoRoute(emf);
    }

    public EndpointGroup addRoutes() {
        return () -> {
            path("", accountRoute.itemRoutes());
            path("", footerRoute.itemRoutes());
            path("", gameRoute.itemRoutes());
            path("", headerRoute.itemRoutes());
            path("", informationRoute.itemRoutes());
            path("", licenseRoute.itemRoutes());
            path("", qaRoute.itemRoutes());
            path("", roleRoute.itemRoutes());
            path("", todoRoute.itemRoutes());
        };
    }
}
