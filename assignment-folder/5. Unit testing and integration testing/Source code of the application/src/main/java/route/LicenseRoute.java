package route;

import controller.LicenseController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class LicenseRoute {
    private static LicenseController controller;

    public LicenseRoute(EntityManagerFactory emf) {
        controller = new LicenseController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> path("/licences", () -> {
            get("/", controller.getAll());
            get("/{id}", controller.getById());
            post("/", controller.create());
            put("/{id}", controller.update());
            delete("/{id}", controller.delete());
        });
    }
}
