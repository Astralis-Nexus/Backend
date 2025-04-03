package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Information;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InformationDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the informations.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Information> informations = informationDAO.getAll();

        // Then
        assertFalse(informations.isEmpty());
        assertEquals(expectedSize, informations.size());
    }

    @Test
    @DisplayName("Create an information with existing account.")
    public void create() {
        // Given
        Account account;
        try (EntityManager em = emf.createEntityManager()) {
            account = em.createQuery("SELECT a FROM Account a WHERE a.username = :name", Account.class)
                    .setParameter("name", "username")
                    .getSingleResult();
        }
        Information informationToCreate = new Information("username", account);
        int expectedId = 2;

        // When
        Information informationCreated = informationDAO.create(informationToCreate);

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
        Information informationUpdated = informationDAO.update(informationToUpdate);

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
        Information informationFound = informationDAO.getById(givenId);

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
        Information deletedInformation = informationDAO.delete(givenId);

        // Then
        assertNotNull(deletedInformation);
        assertEquals(givenId, deletedInformation.getId());
    }
}