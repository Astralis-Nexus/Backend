package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Footer;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FooterDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the footers.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Footer> footers = getDAO(FooterDAO.class).getAll();

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
        Footer footerCreated = getDAO(FooterDAO.class).create(footerToCreate);

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
        Footer footerCreated = getDAO(FooterDAO.class).create(footerToCreate);

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
        Footer footerUpdated = getDAO(FooterDAO.class).update(footerToUpdate);

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
        Footer footerFound = getDAO(FooterDAO.class).getById(givenId);

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
        Footer deletedFooter = getDAO(FooterDAO.class).delete(givenId);

        // Then
        assertNotNull(deletedFooter);
        assertEquals(givenId, deletedFooter.getId());
    }
}