package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {

    private static EntityManagerFactory emf;
    private static AccountDAO dao;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = AccountDAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.persist(new Role(Role.RoleName.REGULAR));
            em.persist(new Role(Role.RoleName.ADMIN));

            Role regularRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", Role.RoleName.REGULAR)
                    .getSingleResult();

            em.persist(new Account("username", "password", regularRole));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Account RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(dao);
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
        List<Account> accounts = dao.getAll();

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
        Account accountCreated = dao.create(accountToCreate);

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
        Account accountUpdated = dao.update(accountToUpdate);

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
        Account accountFound = dao.getById(givenId);

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
        Account deletedAccount = dao.delete(givenId);

        // Then
        assertNotNull(deletedAccount);
        assertEquals(givenId, deletedAccount.getId());
    }
}