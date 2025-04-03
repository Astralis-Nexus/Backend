package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest extends BaseTest {

    @BeforeEach
    public void beforeEach() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Role(Role.RoleName.REGULAR));
            em.persist(new Role(Role.RoleName.ADMIN));

            Role regularRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", Role.RoleName.REGULAR)
                    .getSingleResult();

            em.persist(new Account("username", "password", regularRole));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(accountDAO);
    }

    @Test
    @DisplayName("Testing that entity manager factory is not null.")
    void getEmf() {
        // Then
        assertNotNull(emf);
    }

    @Test
    @DisplayName("Get all the accounts.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Account> accounts = accountDAO.getAll();

        // Then
        assertFalse(accounts.isEmpty());
        assertEquals(expectedSize, accounts.size());
    }

    @Test
    @DisplayName("Create an account with existing role.")
    public void create() {
        // Given
        Account accountToCreate = new Account("username", "password", new Role(Role.RoleName.REGULAR));
        int expectedId = 2;

        // When
        Account accountCreated = accountDAO.create(accountToCreate);

        // Then
        assertNotNull(accountCreated);
        assertEquals(expectedId, accountToCreate.getId());
    }

    @Test
    @DisplayName("Update an existing account.")
    public void update() {
        // Given
        Account accountToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            accountToUpdate = em.find(Account.class, givenId);
            em.getTransaction().commit();
        }

        // When
        accountToUpdate.setUsername("test1)");
        accountToUpdate.setPassword("test2)");
        Account accountUpdated = accountDAO.update(accountToUpdate);

        // Then
        assertNotNull(accountUpdated);
        assertEquals(givenId, accountUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific account.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Account accountFound = accountDAO.getById(givenId);

        // Then
        assertNotNull(accountFound);
        assertEquals(givenId, accountFound.getId());
    }

    @Test
    @DisplayName("Delete an specific account.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Account deletedAccount = accountDAO.delete(givenId);

        // Then
        assertNotNull(deletedAccount);
        assertEquals(givenId, deletedAccount.getId());
    }
}