package controller;

import io.javalin.http.Handler;

public interface IController {
    Handler create();

    Handler getAll();

    Handler getById();

    Handler update();

    Handler delete();
}
