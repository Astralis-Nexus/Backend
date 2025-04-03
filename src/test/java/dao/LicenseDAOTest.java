package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Game;
import persistence.model.License;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LicenseDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the licenses.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<License> licenses = getDAO(LicenseDAO.class).getAll();

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
                    .setParameter("name", "username")
                    .getSingleResult();
        }
        License licenseToCreate = new License("user", "password", "username@mail.dk", game);
        int expectedId = 2;

        // When
        License licenseCreated = getDAO(LicenseDAO.class).create(licenseToCreate);

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
        License licenseUpdated = getDAO(LicenseDAO.class).update(licenseToUpdate);

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
        License licenseFound = getDAO(LicenseDAO.class).getById(givenId);

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
        License deletedLicense = getDAO(LicenseDAO.class).delete(givenId);

        // Then
        assertNotNull(deletedLicense);
        assertEquals(givenId, deletedLicense.getId());
    }
}