package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the accounts.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Account> accounts = getDAO(AccountDAO.class).getAll();

        // Then
        assertFalse(accounts.isEmpty());
        assertEquals(expectedSize, accounts.size());
    }

    @Test
    @DisplayName("Create an account with existing role.")
    public void create() {
        // Given
        Account accountToCreate = new Account("i-username", "password", new Role(Role.RoleName.REGULAR));
        int expectedId = 2;

        // When
        Account accountCreated = getDAO(AccountDAO.class).create(accountToCreate);

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
        Account accountUpdated = getDAO(AccountDAO.class).update(accountToUpdate);

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
        Account accountFound = getDAO(AccountDAO.class).getById(givenId);

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
        Account deletedAccount = getDAO(AccountDAO.class).delete(givenId);

        // Then
        assertNotNull(deletedAccount);
        assertEquals(givenId, deletedAccount.getId());
    }
}