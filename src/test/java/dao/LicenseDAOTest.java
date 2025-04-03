package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.License;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LicenseDAOTest {
    private static EntityManagerFactory emf;
    private static LicenseDAO dao;
    private static Game game;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = LicenseDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(new Role(Role.RoleName.REGULAR));
            em.persist(new Role(Role.RoleName.ADMIN));

            Role regularRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", Role.RoleName.REGULAR)
                    .getSingleResult();

            Account account = new Account("username", "password", regularRole);

            em.persist(account);

            game = new Game("Test", account);
            em.persist(game);
            em.persist(
                    new License("username", "password", "username@email.dk", game));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM License ").executeUpdate();
            em.createQuery("DELETE FROM Game ").executeUpdate();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE License RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(dao);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }

    @Test
    @DisplayName("Get all the licenses.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<License> licenses = dao.getAll();

        // Then
        assertFalse(licenses.isEmpty());
        assertEquals(expectedSize, licenses.size());
    }

    @Test
    @DisplayName("Create an license with existing game.")
    public void create() {
        // Given
        License licenseToCreate = new License("user", "password", "username@mail.dk", game);
        int expectedId = 2;

        // When
        License licenseCreated = dao.create(licenseToCreate);

        // Then
        assertNotNull(licenseCreated);
        assertEquals(expectedId, licenseCreated.getId());
    }

    @Test
    @DisplayName("Update an existing license.")
    public void update() {
        // Given
        License licenseToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            licenseToUpdate = em.find(License.class, givenId);
            em.getTransaction().commit();
        }

        // When
        licenseToUpdate.setEmail("test1)");
        licenseToUpdate.setUsername("test2)");
        licenseToUpdate.setPassword("test3)");
        licenseToUpdate.setPcNumber(1);
        License licenseUpdated = dao.update(licenseToUpdate);

        // Then
        assertNotNull(licenseUpdated);
        assertEquals(givenId, licenseUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific license.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        License licenseFound = dao.getById(givenId);

        // Then
        assertNotNull(licenseFound);
        assertEquals(givenId, licenseFound.getId());
    }

    @Test
    @DisplayName("Delete an specific license.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        License deletedLicense = dao.delete(givenId);

        // Then
        assertNotNull(deletedLicense);
        assertEquals(givenId, deletedLicense.getId());
    }
}