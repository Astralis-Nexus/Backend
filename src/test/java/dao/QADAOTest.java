package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.QA;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QADAOTest extends BaseTest {

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

            em.persist(
                    new QA("username", "password", account));
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(qaDAO);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }

    @Test
    @DisplayName("Get all the qas.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<QA> qas = qaDAO.getAll();

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
        QA qaCreated = qaDAO.create(qaToCreate);

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
        QA qaUpdated = qaDAO.update(qaToUpdate);

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
        QA qaFound = qaDAO.getById(givenId);

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
        QA qaLicense = qaDAO.delete(givenId);

        // Then
        assertNotNull(qaLicense);
        assertEquals(givenId, qaLicense.getId());
    }
}