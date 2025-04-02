package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.QA;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QADAOTest {
    private static EntityManagerFactory emf;
    private static QADAO dao;
    private static Account account;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = QADAO.getInstance(emf);
    }

    @AfterAll
    static void tearDown() {
        emf.close();
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            account = new Account("username", "password", new Role(Role.RoleName.REGULAR));
            em.persist(account);
            em.persist(
                    new QA("username", "password", account));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM QA ").executeUpdate();
            em.createQuery("DELETE FROM Account ").executeUpdate();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE QA RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the qas.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<QA> qas = dao.getAll();

        // Then
        assertFalse(qas.isEmpty());
        assertEquals(expectedSize, qas.size());
    }

    @Test
    @DisplayName("Create an qa with existing account.")
    public void create() {
        // Given
        QA qaToCreate = new QA("user", "password", account);
        int expectedId = 2;

        // When
        QA qaCreated = dao.create(qaToCreate);

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
        QA qaUpdated = dao.update(qaToUpdate);

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
        QA qaFound = dao.getById(givenId);

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
        QA qaLicense = dao.delete(givenId);

        // Then
        assertNotNull(qaLicense);
        assertEquals(givenId, qaLicense.getId());
    }
}