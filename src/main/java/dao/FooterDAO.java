package dao;

import jakarta.persistence.*;
import persistence.model.Footer;
import persistence.model.Role;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FooterDAO implements IDAO<Footer> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static FooterDAO instance;
    private static EntityManagerFactory emf;

    public static FooterDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FooterDAO();
        }
        return instance;
    }

    @Override
    public List<Footer> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Footer> query = em.createQuery("SELECT f FROM Footer f", Footer.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No footers found.");
        }
    }

    @Override
    public Footer getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Footer> query = em.createQuery("SELECT f FROM Footer f WHERE f.id = :id", Footer.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No footer found with the following id: " + id);
        }
    }

    @Override
    public Footer create(Footer footer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Check if the role exists
            Role role = em.find(Role.class, footer.getRole().getName());

            // If the role doesn't exist, persist it
            if (role == null) {
                role = new Role(footer.getRole().getName());
                em.persist(role);
            }

            // Set the role for the account
            footer.setRole(role);

            // Persist the account
            em.persist(footer);

            em.getTransaction().commit();
            return footer;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting footer.");
        }
    }

    @Override
    public Footer update(Footer footer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(footer);
            em.getTransaction().commit();
            return footer;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating footer.");
        }
    }

    @Override
    public Footer delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Footer footer = em.find(Footer.class, id);
            if (footer != null) {
                em.remove(footer);
                em.getTransaction().commit();
            }
            return footer;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error deleting account.");
        }
    }
}
