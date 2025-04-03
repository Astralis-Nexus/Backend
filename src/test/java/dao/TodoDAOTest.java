package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Role;
import persistence.model.Todo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest extends BaseTest {

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
            em.persist(new Todo(LocalDate.now(), "My Task", false, account));
            em.getTransaction().commit();
        }
    }

    @Test
    @DisplayName("Testing that the dao is not null.")
    void getDAO() {
        // Then
        assertNotNull(todoDAO);
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
        List<Todo> todos = todoDAO.getAll();

        // Then
        assertFalse(todos.isEmpty());
        assertEquals(expectedSize, todos.size());
    }

    @Test
    @DisplayName("Create an todo.")
    public void create() {
        // Given
        Account account;
        try (EntityManager em = emf.createEntityManager()) {
            account = em.createQuery("SELECT a FROM Account a WHERE a.username = :name", Account.class)
                    .setParameter("name", "username")
                    .getSingleResult();
        }
        int expectedId = 2;
        Todo todoToCreate = new Todo(LocalDate.now(), "My Task2", false, account);

        // When
        Todo todoCreated = todoDAO.create(todoToCreate);

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
        Todo todoUpdated = todoDAO.update(todoToUpdate);

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
        Todo todoFound = todoDAO.getById(givenId);

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
        Todo deletedTodo = todoDAO.delete(givenId);

        // Then
        assertNotNull(deletedTodo);
        assertEquals(givenId, deletedTodo.getId());
    }
}