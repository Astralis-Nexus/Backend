package dao;

import jakarta.persistence.*;
import persistence.model.QA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QADAO implements IDAO<QA> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static QADAO instance;
    private static EntityManagerFactory emf;

    public static QADAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QADAO();
        }
        return instance;
    }

    @Override
    public List<QA> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<QA> query = em.createQuery("SELECT q FROM QA q", QA.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No QAs found.");
        }
    }

    @Override
    public QA getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<QA> query = em.createQuery("SELECT q FROM QA q WHERE q.id = :id", QA.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No QAs found with the following id: " + id);
        }
    }

    @Override
    public QA create(QA qa) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(qa);
            em.getTransaction().commit();
            return qa;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting QAs.");
        }
    }

    @Override
    public QA update(QA qa) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(qa);
            em.getTransaction().commit();
            return qa;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating QAs.");
        }
    }

    @Override
    public QA delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            QA qa = em.find(QA.class, id);
            if (qa != null) {
                qa.getAccount().getQas().remove(qa);
                qa.setAccount(null);
                em.remove(qa);
                em.getTransaction().commit();
            }
            return qa;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error QAs account.");
        }
    }
}
