package route;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

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

    private EndpointGroup combineRoutes(EndpointGroup... endpointGroups) {
        return () -> {
            for (EndpointGroup group : endpointGroups) {
                group.addEndpoints();
            }
        };
    }

    public EndpointGroup addRoutes() {
        return combineRoutes(accountRoute.itemRoutes(), footerRoute.itemRoutes(), gameRoute.itemRoutes(),
                headerRoute.itemRoutes(), informationRoute.itemRoutes(),
                licenseRoute.itemRoutes(), qaRoute.itemRoutes(), roleRoute.itemRoutes(), todoRoute.itemRoutes());
    }
}
