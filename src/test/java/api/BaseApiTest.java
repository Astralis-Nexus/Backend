package api;

import application.config.ApplicationConfig;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Footer;
import persistence.model.Game;
import persistence.model.Header;
import persistence.model.Information;
import persistence.model.License;
import persistence.model.QA;
import persistence.model.Role;
import persistence.model.Todo;
import route.Route;

import java.time.LocalDate;

public abstract class BaseApiTest {
    private static final int API_TEST_PORT = 7008;
    private static final String API_TEST_BASE_URI = "http://localhost:" + API_TEST_PORT + "/api";
    protected static EntityManagerFactory emf;
    private static ApplicationConfig app;

    @BeforeAll
    static void setUpBeforeAll() {
        RestAssured.baseURI = API_TEST_BASE_URI;
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        Route route = new Route(emf);

        if (app == null) {
            app = ApplicationConfig.getInstance();
            app.initiateServer()
                    .startServer(API_TEST_PORT)
                    .setRoute(route.addRoutes());
        }
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = API_TEST_BASE_URI;
        clearDatabase();
        seedDatabase();
    }

    @AfterEach
    void tearDown() {
        clearDatabase();
    }

    protected void clearDatabase() {
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

    private void seedDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Role regularRole = new Role(Role.RoleName.REGULAR);
            Role noneRole = new Role(Role.RoleName.NONE);
            em.persist(regularRole);
            em.persist(noneRole);

            Footer footer = new Footer("Help", "Seed footer description", regularRole);
            Header header = new Header("Seed header", regularRole);
            Account account = new Account("seed-user", "P@ssw0rd2026", regularRole);
            em.persist(footer);
            em.persist(header);
            em.persist(account);

            Information information = new Information("Seed information text", account, Information.ImportanceLevel.HIGH);
            QA qa = new QA("Seed question?", "Seed answer", account);
            QA e2eQa = new QA(
                    "Hvordan lukker jeg en gammel licens?",
                    "Skift licensens status til INACTIVE og opret en opgave, hvis der skal foelges op.",
                    account
            );
            Todo todo = new Todo(
                    LocalDate.now(),
                    "Seed todo",
                    Todo.Status.PENDING,
                    Todo.Source.GAMEHUB,
                    account
            );
            Todo e2eTodo = new Todo(
                    LocalDate.now().minusDays(1),
                    "Afslut inaktive Rocket League",
                    Todo.Status.COMPLETED,
                    Todo.Source.GAMEHUB,
                    account
            );
            Game game = new Game("SeedGame", account);
            em.persist(information);
            em.persist(qa);
            em.persist(e2eQa);
            em.persist(todo);
            em.persist(e2eTodo);
            em.persist(game);

            License license = new License(
                    "seed-license",
                    "P@ssw0rd2026",
                    "seed@example.com",
                    License.LicenseStatus.ACTIVE,
                    game
            );
            em.persist(license);

            em.getTransaction().commit();
        }
    }

}
