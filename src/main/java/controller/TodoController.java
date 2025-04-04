package controller;

import dao.TodoDAO;
import dto.TodoDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Todo;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoController implements IController {
    //private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timestamp = DateUtil.getTimestamp();
    private final TodoDAO dao;

    public TodoController(EntityManagerFactory emf) {
        this.dao = TodoDAO.getInstance(emf);
    }

    public TodoDTO converter(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .date(todo.getDate())
                .description(todo.getDescription())
                .status(todo.isStatus())
                .done_by(todo.getDone_by())
                .account(todo.getAccount())
                .build();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            if (!dao.getAll().isEmpty()) {
                List<Todo> todos = dao.getAll();
                List<TodoDTO> todoDTOS = new ArrayList<>();
                for (Todo t : todos) {
                    TodoDTO todoDTO = converter(t);
                    todoDTOS.add(todoDTO);
                }
                ctx.json(todoDTOS);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Todo todo = dao.getById(id);
            if (todo != null) {
                TodoDTO todoDTO = converter(todo);
                ctx.json(todoDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Todo todoCreated = ctx.bodyAsClass(Todo.class);
            if (todoCreated != null) {
                Todo todo = dao.create(todoCreated);
                TodoDTO todoDTO = converter(todo);
                ctx.json(todoDTO);
            } else {
                throw new ApiException(500, "No data found. ", timestamp);
            }
        };
    }

    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Todo todoToUpdate = ctx.bodyAsClass(Todo.class);
            todoToUpdate.setId(id);
            Todo todoUpdated = dao.update(todoToUpdate);
            if (todoUpdated != null) {
                TodoDTO todoDTO = converter(todoUpdated);
                ctx.json(todoDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Todo todoDeleted = dao.delete(id);
            if (todoDeleted != null) {
                TodoDTO todoDTO = converter(todoDeleted);
                ctx.json(todoDTO);
            } else {
                throw new ApiException(404, "No data found. ", timestamp);
            }
        };
    }
}
