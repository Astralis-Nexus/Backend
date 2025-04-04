package route;

import controller.QAController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class QARoute {
    private static EntityManagerFactory emf;
    private static QAController controller;

    public QARoute(EntityManagerFactory emf) {
        this.emf = emf;
        controller = new QAController(emf);
    }

    public EndpointGroup itemRoutes() {
        return () -> {
            path("/qas", () -> {
                get("/", controller.getAll());
                get("/{id}", controller.getById());
                post("/", controller.create());
                put("/{id}", controller.update());
                delete("/{id}", controller.delete());
            });
        };
    }
}
