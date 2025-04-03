package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.QA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QADAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the qas.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<QA> qas = getDAO(QADAO.class).getAll();

        // Then
        assertFalse(qas.isEmpty());
        assertEquals(expectedSize, qas.size());
    }

    @Test
    @DisplayName("Create an qa with existing account.")
    public void create() {
        // Given
        Account account;
        try (EntityManager em = emf.createEntityManager()) {
            account = em.createQuery("SELECT a FROM Account a WHERE a.username = :name", Account.class)
                    .setParameter("name", "username")
                    .getSingleResult();
        }
        QA qaToCreate = new QA("user", "password", account);
        int expectedId = 2;

        // When
        QA qaCreated = getDAO(QADAO.class).create(qaToCreate);

        // Then
        assertNotNull(qaCreated);
        assertEquals(expectedId, qaCreated.getId());
    }

    @Test
    @DisplayName("Update an existing qa.")
    public void update() {
        // Given
        QA qaToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            qaToUpdate = em.find(QA.class, givenId);
            em.getTransaction().commit();
        }

        // When
        qaToUpdate.setQuestion("test1)");
        qaToUpdate.setAnswer("test2)");
        QA qaUpdated = getDAO(QADAO.class).update(qaToUpdate);

        // Then
        assertNotNull(qaUpdated);
        assertEquals(givenId, qaUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific qa.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        QA qaFound = getDAO(QADAO.class).getById(givenId);

        // Then
        assertNotNull(qaFound);
        assertEquals(givenId, qaFound.getId());
    }

    @Test
    @DisplayName("Delete an specific qa.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        QA qaLicense = getDAO(QADAO.class).delete(givenId);

        // Then
        assertNotNull(qaLicense);
        assertEquals(givenId, qaLicense.getId());
    }
}