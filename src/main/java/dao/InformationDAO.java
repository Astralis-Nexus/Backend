package dao;

import jakarta.persistence.*;
import persistence.model.Information;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InformationDAO implements IDAO<Information> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static InformationDAO instance;
    private static EntityManagerFactory emf;

    public static InformationDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new InformationDAO();
        }
        return instance;
    }

    @Override
    public List<Information> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Information> query = em.createQuery("SELECT i FROM Information i", Information.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No information found.");
        }
    }

    @Override
    public Information getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Information> query = em.createQuery("SELECT i FROM Information i WHERE i.id = :id", Information.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No information found with the following id: " + id);
        }
    }

    @Override
    public Information create(Information information) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(information);
            em.getTransaction().commit();
            return information;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting information.");
        }
    }

    @Override
    public Information update(Information information) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(information);
            em.getTransaction().commit();
            return information;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating information.");
        }
    }

    @Override
    public Information delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Information information = em.find(Information.class, id);
            if (information != null) {
                information.getAccount().removeInformation(information);
                em.remove(information);
                em.getTransaction().commit();
            }
            return information;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error deleting account.");
        }
    }
}
