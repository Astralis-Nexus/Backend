package route;

import controller.LicenseController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class LicenseRoute {
    private static EntityManagerFactory emf;
    private static LicenseController controller;

    public LicenseRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new LicenseController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/licences", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{license_id}", controller.update());
                delete("/{license_id}", controller.delete());
            });
        };
    }
}
