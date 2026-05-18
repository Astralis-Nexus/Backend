package integration;

import dao.AccountDAO;
import dao.FooterDAO;
import dao.GameDAO;
import dao.HeaderDAO;
import dao.InformationDAO;
import dao.LicenseDAO;
import dao.QADAO;
import dao.RoleDAO;
import dao.TodoDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.Role;

public abstract class BaseIntegrationTest {
    protected static EntityManagerFactory emf;
    protected static AccountDAO accountDAO;
    protected static FooterDAO footerDAO;
    protected static GameDAO gameDAO;
    protected static HeaderDAO headerDAO;
    protected static InformationDAO informationDAO;
    protected static LicenseDAO licenseDAO;
    protected static QADAO qaDAO;
    protected static RoleDAO roleDAO;
    protected static TodoDAO todoDAO;

    protected Role regularRole;

    @BeforeAll
    static void setUpBeforeAll() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        roleDAO = RoleDAO.getInstance(emf);
        accountDAO = AccountDAO.getInstance(emf);
        footerDAO = FooterDAO.getInstance(emf);
        gameDAO = GameDAO.getInstance(emf);
        headerDAO = HeaderDAO.getInstance(emf);
        informationDAO = InformationDAO.getInstance(emf);
        licenseDAO = LicenseDAO.getInstance(emf);
        qaDAO = QADAO.getInstance(emf);
        todoDAO = TodoDAO.getInstance(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            regularRole = new Role(Role.RoleName.REGULAR);
            em.persist(regularRole);
            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE license RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE todo RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE qa RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE information RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE game RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE header RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE footer RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE account RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE role RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    protected Account createAccount(String username) {
        return accountDAO.create(new Account(username, "P@ssw0rd2026", regularRole));
    }

    protected Game createGame(String name) {
        Account account = createAccount("game-owner");
        return gameDAO.create(new Game(name, account));
    }
}
