package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Footer;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FooterDAOTest {

    private static EntityManagerFactory emf;
    private static FooterDAO dao;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = FooterDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(
                    new Footer("Header", "Description", new Role(Role.RoleName.REGULAR)));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Footer ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Footer RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the footers.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Footer> footers = dao.getAll();

        // Then
        assertFalse(footers.isEmpty());
        assertEquals(expectedSize, footers.size());
    }

    @Test
    @DisplayName("Create an footer with existing role.")
    public void create() {
        // Given
        Footer footerToCreate = new Footer("Header", "Description", new Role(Role.RoleName.REGULAR));
        int expectedId = 2;

        // When
        Footer footerCreated = dao.create(footerToCreate);

        // Then
        assertNotNull(footerCreated);
        assertEquals(expectedId, footerCreated.getId());
    }

    @Test
    @DisplayName("Create an footer with non existing role.")
    public void create2() {
        // Given
        Footer footerToCreate = new Footer("Header", "Description", new Role(Role.RoleName.REGULAR));
        int expectedId = 2;

        // When
        Footer footerCreated = dao.create(footerToCreate);

        // Then
        assertNotNull(footerCreated);
        assertEquals(expectedId, footerCreated.getId());
    }

    @Test
    @DisplayName("Update an existing footer.")
    public void update() {
        // Given
        Footer footerToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            footerToUpdate = em.find(Footer.class, givenId);
            em.getTransaction().commit();
        }

        // When
        footerToUpdate.setHeader("test1)");
        footerToUpdate.setDescription("test2)");
        Footer footerUpdated = dao.update(footerToUpdate);

        // Then
        assertNotNull(footerUpdated);
        assertEquals(givenId, footerUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific footer.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Footer footerFound = dao.getById(givenId);

        // Then
        assertNotNull(footerFound);
        assertEquals(givenId, footerFound.getId());
    }

    @Test
    @DisplayName("Delete an specific footer.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Footer deletedFooter = dao.delete(givenId);

        // Then
        assertNotNull(deletedFooter);
        assertEquals(givenId, deletedFooter.getId());
    }
}