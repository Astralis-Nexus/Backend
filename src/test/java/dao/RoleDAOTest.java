package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleDAOTest {
    private static EntityManagerFactory emf;
    private static RoleDAO dao;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = RoleDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(new Role(Role.RoleName.ADMIN));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Role RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the roles.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Role> roles = dao.getAll();

        // Then
        assertFalse(roles.isEmpty());
        assertEquals(expectedSize, roles.size());
        assertEquals(expectedSize, roles.get(0).getId());
    }

    @Test
    @DisplayName("Create an role.")
    public void create() {
        // Given
        Role roleToCreate = new Role(Role.RoleName.REGULAR);
        String expectedId = Role.RoleName.REGULAR.toString();

        // When
        Role roleCreated = dao.create(roleToCreate);

        // Then
        assertNotNull(roleCreated);
        assertEquals(expectedId, roleCreated.getName().toString());
    }

    @Test
    @DisplayName("Update an existing role.")
    public void update() {
        // Given
        Role roleToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            roleToUpdate = em.find(Role.class, givenId);
            em.getTransaction().commit();
        }

        // When
        Role roleUpdated = dao.update(roleToUpdate);

        // Then
        assertNotNull(roleUpdated);
        assertEquals(givenId, roleUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific role.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Role roleFound = dao.getById(givenId);

        // Then
        assertNotNull(roleFound);
        assertEquals(givenId, roleFound.getId());
    }

    @Test
    @DisplayName("Delete an specific role.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Role deletedRole = dao.delete(givenId);

        // Then
        assertNotNull(deletedRole);
        assertEquals(givenId, deletedRole.getId());
    }
}