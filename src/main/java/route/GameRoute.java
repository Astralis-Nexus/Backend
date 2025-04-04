package route;

import controller.GameController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class GameRoute {
    private static EntityManagerFactory emf;
    private static GameController controller;

    public GameRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new GameController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/games", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
