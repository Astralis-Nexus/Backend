package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Footer;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FooterDAOTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(
                    new Footer("Header", "Description", new Role(Role.RoleName.REGULAR)));
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(footerDAO);
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
        List<Footer> footers = footerDAO.getAll();

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
        Footer footerCreated = footerDAO.create(footerToCreate);

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
        Footer footerCreated = footerDAO.create(footerToCreate);

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
        Footer footerUpdated = footerDAO.update(footerToUpdate);

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
        Footer footerFound = footerDAO.getById(givenId);

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
        Footer deletedFooter = footerDAO.delete(givenId);

        // Then
        assertNotNull(deletedFooter);
        assertEquals(givenId, deletedFooter.getId());
    }
}