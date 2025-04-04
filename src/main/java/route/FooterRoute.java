package route;

import controller.FooterController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class FooterRoute {
    private static EntityManagerFactory emf;
    private static FooterController controller;

    public FooterRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new FooterController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/footers", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
