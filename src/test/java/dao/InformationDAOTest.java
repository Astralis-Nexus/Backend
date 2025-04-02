package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Information;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InformationDAOTest {
    private static EntityManagerFactory emf;
    private static InformationDAO dao;
    private static Account account;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = InformationDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            account = new Account("username", "password", new Role(Role.RoleName.REGULAR));
            em.getTransaction().begin();
            em.persist(account);
            em.persist(
                    new Information("username", account));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Information ").executeUpdate();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Information RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the informations.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Information> informations = dao.getAll();

        // Then
        assertFalse(informations.isEmpty());
        assertEquals(expectedSize, informations.size());
    }

    @Test
    @DisplayName("Create an information with existing account.")
    public void create() {
        // Given
        Information informationToCreate = new Information("username", account);
        int expectedId = 2;

        // When
        Information informationCreated = dao.create(informationToCreate);

        // Then
        assertNotNull(informationCreated);
        assertEquals(expectedId, informationCreated.getId());
    }

    @Test
    @DisplayName("Update an existing information.")
    public void update() {
        // Given
        Information informationToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            informationToUpdate = em.find(Information.class, givenId);
            em.getTransaction().commit();
        }

        // When
        informationToUpdate.setDescription("test1)");
        Information informationUpdated = dao.update(informationToUpdate);

        // Then
        assertNotNull(informationUpdated);
        assertEquals(givenId, informationUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific information.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Information informationFound = dao.getById(givenId);

        // Then
        assertNotNull(informationFound);
        assertEquals(givenId, informationFound.getId());
    }

    @Test
    @DisplayName("Delete an specific information.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Information deletedInformation = dao.delete(givenId);

        // Then
        assertNotNull(deletedInformation);
        assertEquals(givenId, deletedInformation.getId());
    }
}