package dao;

import jakarta.persistence.EntityManagerFactory;
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
}
