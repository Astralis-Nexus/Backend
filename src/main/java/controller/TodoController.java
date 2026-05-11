package controller;

import dao.TodoDAO;
import dto.TodoDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Todo;
import utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TodoController implements IController {
    private static final String timestamp = DateUtil.getTimestamp();
    private final TodoDAO dao;

    public TodoController(EntityManagerFactory emf) {
        this.dao = TodoDAO.getInstance(emf);
    }

    public TodoDTO converter(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .date(todo.getDate())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .source(todo.getSource())
                .done_by(todo.getDoneBy())
                .account(todo.getAccount())
                .build();
    }

    @Override
    public Handler create() {
        return ctx -> {
            TodoDTO incoming = ctx.bodyAsClass(TodoDTO.class);

            if (incoming == null ||
                    incoming.getAccount() == null ||
                    incoming.getAccount().getId() == null) {
                throw new ApiException(400, "Account ID is required.", timestamp);
            }

            int accountId = incoming.getAccount().getId();
            Account account = dao.getAccountById(accountId);

            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + accountId, timestamp);
            }

            Todo todo = new Todo(
                    incoming.getDate(),
                    incoming.getDescription(),
                    incoming.getStatus(),
                    incoming.getSource(),
                    incoming.getDone_by(),
                    account);

            Todo createdTodo = dao.create(todo);

            TodoDTO responseDTO = TodoDTO.builder()
                    .id(createdTodo.getId())
                    .date(createdTodo.getDate())
                    .description(createdTodo.getDescription())
                    .status(createdTodo.getStatus())
                    .source(createdTodo.getSource())
                    .done_by(createdTodo.getDoneBy())
                    .account(createdTodo.getAccount())
                    .build();

            ctx.json(responseDTO);
        };
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
    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            Todo existingTodo = dao.getById(id);
            if (existingTodo == null) {
                throw new ApiException(404, "Todo not found.", timestamp);
            }

            Todo todoFromRequest = ctx.bodyAsClass(Todo.class);

            if (todoFromRequest.getDescription() != null) {
                existingTodo.setDescription(todoFromRequest.getDescription());
            }
            if (todoFromRequest.getStatus() != null) {
                existingTodo.setStatus(todoFromRequest.getStatus());
            }
            if (todoFromRequest.getSource() != null) {
                existingTodo.setSource(todoFromRequest.getSource());
            }

            
            Todo updated = dao.update(existingTodo);

            if (updated != null) {
                TodoDTO dto = converter(updated);
                ctx.json(dto);
            } else {
                throw new ApiException(404, "No data found.", timestamp);
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
