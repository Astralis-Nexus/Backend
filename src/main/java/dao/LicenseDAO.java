package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.License;

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
}
