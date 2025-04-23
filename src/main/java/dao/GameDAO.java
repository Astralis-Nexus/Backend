package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Game;

public class GameDAO extends DAO<Game> {

    private static GameDAO instance;

    private GameDAO() {
        super(Game.class);
    }

    public static GameDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GameDAO();
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
