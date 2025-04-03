package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import persistence.config.HibernateConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
}
