package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Header;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeaderDAOTest {

    private static EntityManagerFactory emf;
    private static HeaderDAO dao;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = HeaderDAO.getInstance(emf);
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Role role = new Role(Role.RoleName.REGULAR);
            em.persist(role);
            em.persist(
                    new Header("username", role));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Header ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Header RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the headers.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Header> headers = dao.getAll();

        // Then
        assertFalse(headers.isEmpty());
        assertEquals(expectedSize, headers.size());
    }

    @Test
    @DisplayName("Create an header with existing role.")
    public void create() {
        // Given
        Header headerToCreate = new Header("username", new Role(Role.RoleName.REGULAR));
        int expectedId = 2;

        // When
        Header headerCreated = dao.create(headerToCreate);

        // Then
        assertNotNull(headerCreated);
        assertEquals(expectedId, headerCreated.getId());
    }

    @Test
    @DisplayName("Update an existing header.")
    public void update() {
        // Given
        Header headerToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            headerToUpdate = em.find(Header.class, givenId);
            em.getTransaction().commit();
        }

        // When
        headerToUpdate.setName("test1)");
        Header headerUpdated = dao.update(headerToUpdate);

        // Then
        assertNotNull(headerUpdated);
        assertEquals(givenId, headerUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific account.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Header headerFound = dao.getById(givenId);

        // Then
        assertNotNull(headerFound);
        assertEquals(givenId, headerFound.getId());
    }

    @Test
    @DisplayName("Delete an specific header.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Header headerAccount = dao.delete(givenId);

        // Then
        assertNotNull(headerAccount);
        assertEquals(givenId, headerAccount.getId());
    }
}