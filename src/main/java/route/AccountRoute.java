package route;

import controller.AccountController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AccountRoute {
    private static EntityManagerFactory emf;
    private static AccountController controller;

    public AccountRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new AccountController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/accounts", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
