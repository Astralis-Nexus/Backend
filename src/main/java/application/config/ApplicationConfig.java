package application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationConfig {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());
    private static ApplicationConfig instance;
    private ObjectMapper om = new ObjectMapper();
    private Javalin app;

    private ApplicationConfig() {
        ObjectMapper om = new ObjectMapper();
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.routing.contextPath = "/api";
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                    //it.allowHost("https://cphbusinessprojekt.dk");
                });
            });
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

    public ApplicationConfig setExceptionHandlers() {
        app.exception(Exception.class, (e, ctx) -> {
            ObjectNode node = om.createObjectNode().put("errorMessage from application config", e.getMessage());
            ctx.status(500).json(node);
        });

        app.error(404, ctx -> {
            ctx.status(404).result("Not Found");
        });

        app.exception(IllegalStateException.class, (e, ctx) -> {
            ctx.status(400).result("Bad Request: " + e.getMessage());
        });
        return instance;
    }

    public void stopServer() {
        app.stop();
    }
}
