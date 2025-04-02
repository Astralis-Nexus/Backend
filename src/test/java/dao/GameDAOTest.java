package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    private static EntityManagerFactory emf;
    private static GameDAO gameDAO;
    private static AccountDAO accountDAO;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        gameDAO = GameDAO.getInstance(emf);
        accountDAO = AccountDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            Account account = new Account("username", "password", new Role(Role.RoleName.REGULAR));
            em.getTransaction().begin();
            em.persist(account);
            em.persist(
                    new Game("username", account));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Game ").executeUpdate();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Game RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(gameDAO);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }

    @Test
    @DisplayName("Get all the games.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Game> games = gameDAO.getAll();

        // Then
        assertFalse(games.isEmpty());
        assertEquals(expectedSize, games.size());
    }

    @Test
    @DisplayName("Create an game with existing account.")
    public void create() {
        // Given
        Account account2 = new Account("username", "password", new Role(Role.RoleName.REGULAR));
        accountDAO.create(account2);
        Game gameToCreate = new Game("username1", account2);
        int expectedId = 2;

        // When
        Game gameCreated = gameDAO.create(gameToCreate);

        // Then
        assertNotNull(gameCreated);
        assertEquals(expectedId, gameCreated.getId());
    }

    @Test
    @DisplayName("Update an existing game.")
    public void update() {
        // Given
        Game gameToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            gameToUpdate = em.find(Game.class, givenId);
            em.getTransaction().commit();
        }

        // When
        gameToUpdate.setName("test1)");
        Game gameUpdated = gameDAO.update(gameToUpdate);

        // Then
        assertNotNull(gameUpdated);
        assertEquals(givenId, gameUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific game.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Game gameFound = gameDAO.getById(givenId);

        // Then
        assertNotNull(gameFound);
        assertEquals(givenId, gameFound.getId());
    }

    @Test
    @DisplayName("Delete an specific game.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Game deletedGame = gameDAO.delete(givenId);

        // Then
        assertNotNull(deletedGame);
        assertEquals(givenId, deletedGame.getId());
    }
}