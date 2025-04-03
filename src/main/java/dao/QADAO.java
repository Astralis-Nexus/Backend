package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.QA;
import utility.DateUtil;

public class QADAO extends DAO<QA> {

    private static QADAO instance;

    private QADAO() {
        super(QA.class, DateUtil.getDateFormat());
    }

    public static QADAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QADAO();
        }
        return instance;
    }
}
