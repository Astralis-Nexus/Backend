package dao;

import jakarta.persistence.*;
import persistence.model.Todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodoDAO implements IDAO<Todo> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static TodoDAO instance;
    private static EntityManagerFactory emf;

    public static TodoDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TodoDAO();
        }
        return instance;
    }

    @Override
    public List<Todo> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Todo> query = em.createQuery("SELECT t FROM Todo t", Todo.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No todos found.");
        }
    }

    @Override
    public Todo getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Todo> query = em.createQuery("SELECT t FROM Todo t WHERE t.id = :id", Todo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No todos found with the following id: " + id);
        }
    }

    @Override
    public Todo create(Todo todo) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(todo);
            em.getTransaction().commit();
            return todo;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting todos.");
        }
    }

    @Override
    public Todo update(Todo todo) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(todo);
            em.getTransaction().commit();
            return todo;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating todos.");
        }
    }

    @Override
    public Todo delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Todo todo = em.find(Todo.class, id);
            if (todo != null) {
                todo.getAccount().getTodos().remove(todo);
                em.remove(todo);
                em.getTransaction().commit();
            }
            return todo;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error todos account.");
        }
    }
}
