package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Todo;

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
      public Account getAccountById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }
}
