package dao;

import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import utility.DateUtil;

public class AccountDAO extends DAO<Account> {

    private static AccountDAO instance;

    private AccountDAO() {
        super(Account.class);
    }

    public static AccountDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AccountDAO();
        }
        return instance;
    }

}
