package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.License;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LicenseDAOTest extends BaseTest {

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

            Game game = new Game("Test", account);
            em.persist(game);
            em.persist(
                    new License("username", "password", "username@email.dk", game));
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(licenseDAO);
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
        List<License> licenses = licenseDAO.getAll();

        // Then
        assertFalse(licenses.isEmpty());
        assertEquals(expectedSize, licenses.size());
    }

    @Test
    @DisplayName("Create an license with existing game.")
    public void create() {
        // Given
        Game game;
        try (EntityManager em = emf.createEntityManager()) {
            game = em.createQuery("SELECT g FROM Game g WHERE g.name = :name", Game.class)
                    .setParameter("name", "Test")
                    .getSingleResult();
        }
        License licenseToCreate = new License("user", "password", "username@mail.dk", game);
        int expectedId = 2;

        // When
        License licenseCreated = licenseDAO.create(licenseToCreate);

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
        License licenseUpdated = licenseDAO.update(licenseToUpdate);

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
        License licenseFound = licenseDAO.getById(givenId);

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
        License deletedLicense = licenseDAO.delete(givenId);

        // Then
        assertNotNull(deletedLicense);
        assertEquals(givenId, deletedLicense.getId());
    }
}