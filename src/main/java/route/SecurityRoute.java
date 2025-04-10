package route;

import controller.SecurityController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class SecurityRoute {
    private final SecurityController securityController;

    public SecurityRoute(EntityManagerFactory emf) {
        this.securityController = new SecurityController(emf);
    }

    public EndpointGroup authRoutes() {
        return () -> path("/security", () -> {
            post("/login", securityController.login());
            post("/register", securityController.login());
        });
    }
}
