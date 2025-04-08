package application.config;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.plugin.bundled.CorsPluginConfig;


public class ApplicationConfig {
    private static ApplicationConfig instance;
    private final Javalin app;

    private ApplicationConfig() {
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.routing.contextPath = "/api";
            config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
        });
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
