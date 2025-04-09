package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;
import persistence.model.Account;
import java.util.List;



public class AccountDAO extends DAO<Account> {

    private static AccountDAO instance;
    private static EntityManagerFactory emf;

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

    public Account verifyLogin(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            Account account = em.createQuery(
                            "SELECT a FROM Account a WHERE a.username = :username", Account.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return password.equals(account.getPassword()) ? account : null;
        } catch (NoResultException e) {
            return null;
        }
    }
}
