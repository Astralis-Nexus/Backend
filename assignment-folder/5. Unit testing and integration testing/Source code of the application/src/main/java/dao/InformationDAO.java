package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Information;

public class InformationDAO extends DAO<Information> {

    private static InformationDAO instance;

    private InformationDAO() {
        super(Information.class);
    }

    public static InformationDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new InformationDAO();
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
