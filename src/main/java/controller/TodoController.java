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
                .status(todo.getStatus()) // fixed here
                .source(todo.getSource()) // added this
                .done_by(todo.getDone_by())
                .accountId(todo.getAccount().getId())
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
            Todo incoming = ctx.bodyAsClass(Todo.class);
    
            if (incoming == null ||
                incoming.getDescription() == null ||
                incoming.getAccount() == null ||
                incoming.getAccount().getId() == null ||
                incoming.getStatus() == null ||
                incoming.getSource() == null) {
                throw new ApiException(400, "Missing required fields: description, account ID, status, or source", timestamp);
            }
    
            int accountId = incoming.getAccount().getId();
            Account account = dao.getAccountById(accountId);
            if (account == null) {
                throw new ApiException(404, "Account not found with ID: " + accountId, timestamp);
            }
    
            Todo todo = new Todo();
            todo.setDescription(incoming.getDescription());
            todo.setStatus(incoming.getStatus());
            todo.setSource(incoming.getSource());
            todo.setDone_by(incoming.getDone_by());
            todo.setDate(incoming.getDate());
            todo.setAccount(account);
    
            Todo created = dao.create(todo);
    
            TodoDTO dto = new TodoDTO(
                created.getId(),
                created.getDate(),
                created.getDescription(),
                created.getStatus(),
                created.getSource(),
                created.getDone_by(),
                created.getAccount().getId()
            );
    
            ctx.json(dto);
        };
    }
    

@Override
public Handler update() {
    return ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Todo todoToUpdate = ctx.bodyAsClass(Todo.class);
        todoToUpdate.setId(id);

        Account account = dao.getAccountById(todoToUpdate.getAccount().getId());
        if (account == null) {
            throw new ApiException(404, "Account not found", timestamp);
        }
        todoToUpdate.setAccount(account);

        Todo updated = dao.update(todoToUpdate);
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
