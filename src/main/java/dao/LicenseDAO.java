package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.License;
import persistence.model.Game;


public class LicenseDAO extends DAO<License> {

    private static LicenseDAO instance;

    private LicenseDAO() {
        super(License.class);
    }

    public static LicenseDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new LicenseDAO();
        }
        return instance;
    }
       public Game getGameById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Game.class, id);
        } finally {
            em.close();
        }
    }
}
