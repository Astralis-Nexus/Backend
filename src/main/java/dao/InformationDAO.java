package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Information;
import utility.DateUtil;

public class InformationDAO extends DAO<Information> {

    private static InformationDAO instance;

    private InformationDAO() {
        super(Information.class, DateUtil.getDateFormat());
    }

    public static InformationDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new InformationDAO();
        }
        return instance;
    }
}
