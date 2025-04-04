package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Footer;
import utility.DateUtil;

public class FooterDAO extends DAO<Footer> {

    private static FooterDAO instance;

    private FooterDAO() {
        super(Footer.class);
    }

    public static FooterDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FooterDAO();
        }
        return instance;
    }
}
