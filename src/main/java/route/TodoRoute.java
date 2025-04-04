package route;

import controller.TodoController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TodoRoute {
    private static EntityManagerFactory emf;
    private static TodoController controller;

    public TodoRoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new TodoController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/todos", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
