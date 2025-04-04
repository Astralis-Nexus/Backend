package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Todo;
import utility.DateUtil;

public class TodoDAO extends DAO<Todo> {

    private static TodoDAO instance;

    private TodoDAO() {
        super(Todo.class);
    }

    public static TodoDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TodoDAO();
        }
        return instance;
    }
}
