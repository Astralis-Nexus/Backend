package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.QA;

public class QADAO extends DAO<QA> {

    private static QADAO instance;

    private QADAO() {
        super(QA.class);
    }

    public static QADAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QADAO();
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
