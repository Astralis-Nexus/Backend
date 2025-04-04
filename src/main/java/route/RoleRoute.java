package route;

import controller.RoleController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoleRoute {
    private static EntityManagerFactory emf;
    private static RoleController controller;

    public RoleRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new RoleController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/roles", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
