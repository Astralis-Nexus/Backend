package application.config;

import exception.ApiException;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.plugin.bundled.CorsPluginConfig;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;

import java.util.Map;


public class ApplicationConfig {
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static ApplicationConfig instance;
    private final Javalin app;

    private ApplicationConfig() {
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.routing.contextPath = "/api";
            config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
        });
        app.exception(ApiException.class, (e, ctx) -> ctx.status(e.getStatusCode()).json(Map.of(
                STATUS, e.getStatusCode(),
                MESSAGE, e.getMessage(),
                TIMESTAMP, e.getTimeStamp()
        )));
        app.exception(EntityNotFoundException.class, (e, ctx) -> ctx.status(404).json(Map.of(
                STATUS, 404,
                MESSAGE, e.getMessage()
        )));
        app.exception(IllegalArgumentException.class, (e, ctx) -> ctx.status(400).json(Map.of(
                STATUS, 400,
                MESSAGE, e.getMessage()
        )));
        app.exception(PersistenceException.class, (e, ctx) -> ctx.status(409).json(Map.of(
                STATUS, 409,
                MESSAGE, e.getMessage()
        )));
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public ApplicationConfig initiateServer() {
        return instance;
    }

    public ApplicationConfig startServer(int portNumber) {
        app.start(portNumber);
        return instance;
    }

    public ApplicationConfig setRoute(EndpointGroup route) {
        app.routes(route);
        return instance;
    }
}
