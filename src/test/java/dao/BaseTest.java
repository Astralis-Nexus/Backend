package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseTest {

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

    @BeforeAll
    public static void globalSetUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        accountDAO = AccountDAO.getInstance(emf);
        footerDAO = FooterDAO.getInstance(emf);
        gameDAO = GameDAO.getInstance(emf);
        headerDAO = HeaderDAO.getInstance(emf);
        informationDAO = InformationDAO.getInstance(emf);
        licenseDAO = LicenseDAO.getInstance(emf);
        qaDAO = QADAO.getInstance(emf);
        roleDAO = RoleDAO.getInstance(emf);
        todoDAO = TodoDAO.getInstance(emf);
    }

    @AfterAll
    public static void globalTearDown() {
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(new Role(Role.RoleName.REGULAR));
            em.persist(new Role(Role.RoleName.NONE));

            Role regularRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", Role.RoleName.REGULAR)
                    .getSingleResult();

            Account account = new Account("username", "password", regularRole);
            em.persist(account);

            em.persist(new Footer("Header", "Description", regularRole));
            Game game = new Game("username", account);
            em.persist(game);

            em.persist(new Header("username", regularRole));
            em.persist(new Information("username", account));
            em.persist(new License("username", "password", "username@email.dk", game));
            em.persist(new QA("username", "password", account));

            em.persist(new Todo(LocalDate.now(), "My Task", false, account));

            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM License ").executeUpdate();
            em.createQuery("DELETE FROM Game ").executeUpdate();
            em.createQuery("DELETE FROM Information ").executeUpdate();
            em.createQuery("DELETE FROM QA ").executeUpdate();
            em.createQuery("DELETE FROM Todo").executeUpdate();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Footer ").executeUpdate();
            em.createQuery("DELETE FROM Header ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();

            em.createNativeQuery("TRUNCATE TABLE Account RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Information RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE QA RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Role RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Footer RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Header RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Game RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE License RESTART IDENTITY CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Todo RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(accountDAO);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }
}
