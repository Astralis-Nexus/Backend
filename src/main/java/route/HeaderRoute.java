package route;

import controller.HeaderController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HeaderRoute {
    private static EntityManagerFactory emf;
    private static HeaderController controller;

    public HeaderRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new HeaderController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/headers", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
