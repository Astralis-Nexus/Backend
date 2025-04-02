package route;

import controller.InformationController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class InformationRoute {
    private static EntityManagerFactory emf;
    private static InformationController controller;

    public InformationRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new InformationController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/informations", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{information_id}", controller.update());
                delete("/{information_id}", controller.delete());
            });
        };
    }
}
