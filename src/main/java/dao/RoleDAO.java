package dao;

import jakarta.persistence.*;
import persistence.model.Role;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class RoleDAO implements IDAO<Role> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static RoleDAO instance;
    private static EntityManagerFactory emf;

    public static RoleDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RoleDAO();
        }
        return instance;
    }

    @Override
    public List<Role> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r", Role.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No roles found.");
        }
    }

    @Override
    public Role getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.id = :id", Role.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(LocalDateTime.now() + ": No roles found with the following id: " + id);
        }
    }

    @Override
    public Role create(Role role) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
            return role;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting role.");
        }
    }

    @Override
    public Role update(Role role) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(role);
            em.getTransaction().commit();
            return role;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating roles.");
        }
    }

    @Override
    public Role delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Role role = em.find(Role.class, id);
            if (role != null) {
                em.remove(role);
            }
            em.getTransaction().commit();
            return role;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error roles account.");
        }
    }
}
