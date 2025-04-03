package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.*;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseTest {

    protected static EntityManagerFactory emf;
    protected static Map<Class<?>, Object> daos;

    @BeforeAll
    public static void globalSetUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        daos = Map.of(
                AccountDAO.class, AccountDAO.getInstance(emf),
                FooterDAO.class, FooterDAO.getInstance(emf),
                GameDAO.class, GameDAO.getInstance(emf),
                HeaderDAO.class, HeaderDAO.getInstance(emf),
                InformationDAO.class, InformationDAO.getInstance(emf),
                LicenseDAO.class, LicenseDAO.getInstance(emf),
                QADAO.class, QADAO.getInstance(emf),
                RoleDAO.class, RoleDAO.getInstance(emf),
                TodoDAO.class, TodoDAO.getInstance(emf)
        );
    }

    protected static <T> T getDAO(Class<T> daoClass) {
        return daoClass.cast(daos.get(daoClass));
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

            em.persist(new Footer("Header", "Description", regularRole));
            em.persist(new Header("username", regularRole));

            Account account = new Account("username", "password", regularRole);
            em.persist(account);

            em.persist(new Information("username", account));
            em.persist(new QA("username", "password", account));
            em.persist(new Todo(LocalDate.now(), "My Task", false, account));

            Game game = new Game("username", account);
            em.persist(game);

            em.persist(new License("username", "password", "username@email.dk", game));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
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
        assertNotNull(getDAO(AccountDAO.class));
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }
}
