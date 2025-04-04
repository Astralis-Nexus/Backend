package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Role;
import utility.DateUtil;

public class RoleDAO extends DAO<Role> {

    private static RoleDAO instance;

    private RoleDAO() {
        super(Role.class);
    }

    public static RoleDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RoleDAO();
        }
        return instance;
    }
}
