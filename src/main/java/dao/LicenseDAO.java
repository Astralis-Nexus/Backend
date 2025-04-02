package dao;

import jakarta.persistence.*;
import persistence.model.License;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LicenseDAO implements IDAO<License> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static LicenseDAO instance;
    private static EntityManagerFactory emf;

    public static LicenseDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new LicenseDAO();
        }
        return instance;
    }

    @Override
    public List<License> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<License> query = em.createQuery("SELECT l FROM License l", License.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No licenses found.");
        }
    }

    @Override
    public License getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<License> query = em.createQuery("SELECT l FROM License l WHERE l.id = :id", License.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No license found with the following id: " + id);
        }
    }

    @Override
    public License create(License license) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(license);
            em.getTransaction().commit();
            return license;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting license.");
        }
    }

    @Override
    public License update(License license) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(license);
            em.getTransaction().commit();
            return license;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating license.");
        }
    }

    @Override
    public License delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            License license = em.find(License.class, id);
            if (license != null) {
                em.remove(license);
                em.getTransaction().commit();
            }
            return license;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error license account.");
        }
    }
}
