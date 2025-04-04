package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Todo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the Todos.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Todo> todos = getDAO(TodoDAO.class).getAll();

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
        Todo todoCreated = getDAO(TodoDAO.class).create(todoToCreate);

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
        Todo todoUpdated = getDAO(TodoDAO.class).update(todoToUpdate);

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
        Todo todoFound = getDAO(TodoDAO.class).getById(givenId);

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
        Todo deletedTodo = getDAO(TodoDAO.class).delete(givenId);

        // Then
        assertNotNull(deletedTodo);
        assertEquals(givenId, deletedTodo.getId());
    }
}