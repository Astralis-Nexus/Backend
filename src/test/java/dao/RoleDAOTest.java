package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleDAOTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(new Role(Role.RoleName.ADMIN));
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(roleDAO);
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
        List<Role> roles = roleDAO.getAll();

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
        Role roleCreated = roleDAO.create(roleToCreate);

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
        Role roleUpdated = roleDAO.update(roleToUpdate);

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
        Role roleFound = roleDAO.getById(givenId);

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
        Role deletedRole = roleDAO.delete(givenId);

        // Then
        assertNotNull(deletedRole);
        assertEquals(givenId, deletedRole.getId());
    }
}