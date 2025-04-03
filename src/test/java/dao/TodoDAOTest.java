package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import persistence.config.HibernateConfig;
import persistence.model.Account;
import persistence.model.Role;
import persistence.model.Todo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest {
    private static EntityManagerFactory emf;
    private static TodoDAO dao;
    private static Account account;

    @BeforeAll
    static void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        dao = TodoDAO.getInstance(emf);
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

            account = new Account("username", "password", regularRole);

            em.persist(account);
            em.persist(new Todo(LocalDate.now(), "My Task", false, account));
            em.getTransaction().commit();
        }
    }

    @AfterEach
    public void afterEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Todo").executeUpdate();
            em.createQuery("DELETE FROM Account").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE Todo RESTART IDENTITY CASCADE").executeUpdate();
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
    @DisplayName("Get all the Todos.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Todo> todos = dao.getAll();

        // Then
        assertFalse(todos.isEmpty());
        assertEquals(expectedSize, todos.size());
    }

    @Test
    @DisplayName("Create an todo.")
    public void create() {
        // Given
        int expectedId = 2;
        Todo todoToCreate = new Todo(LocalDate.now(), "My Task2", false, account);

        // When
        Todo todoCreated = dao.create(todoToCreate);

        // Then
        assertNotNull(todoCreated);
        assertEquals(expectedId, todoCreated.getId());
    }

    @Test
    @DisplayName("Update an existing todo.")
    public void update() {
        // Given
        Todo todoToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            todoToUpdate = em.find(Todo.class, givenId);
            em.getTransaction().commit();
        }

        // When
        Todo todoUpdated = dao.update(todoToUpdate);

        // Then
        assertNotNull(todoUpdated);
        assertEquals(givenId, todoUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific todo.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Todo todoFound = dao.getById(givenId);

        // Then
        assertNotNull(todoFound);
        assertEquals(givenId, todoFound.getId());
    }

    @Test
    @DisplayName("Delete an specific todo.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Todo deletedTodo = dao.delete(givenId);

        // Then
        assertNotNull(deletedTodo);
        assertEquals(givenId, deletedTodo.getId());
    }
}