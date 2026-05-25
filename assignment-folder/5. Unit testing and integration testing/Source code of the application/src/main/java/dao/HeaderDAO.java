package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Header;

public class HeaderDAO extends DAO<Header> {

    private static HeaderDAO instance;

    private HeaderDAO() {
        super(Header.class);
    }

    public static HeaderDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HeaderDAO();
        }
        return instance;
    }
}
