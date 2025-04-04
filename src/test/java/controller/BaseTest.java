package controller;

import application.config.ApplicationConfig;
import dao.AccountDAO;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.*;
import route.Route;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseTest {
    protected static EntityManagerFactory emf;
    protected static ApplicationConfig app;

    @BeforeAll
    static void setUpBeforeAll() {
        RestAssured.baseURI = "http://localhost:7007/api";
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        Route route = new Route(emf);

        if (app == null) {
            app = ApplicationConfig.getInstance();
            app.initiateServer()
                    .startServer(7007)
                    .setExceptionHandlers()
                    .setRoute(route.addRoutes());
        }
    }

    @BeforeEach
    void setUp() {
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
    void shutDown() {
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
    @DisplayName("Testing that the app is not null.")
    void getDAO() {
        // Then
        assertNotNull(app);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }
}
