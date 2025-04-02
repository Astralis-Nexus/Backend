package dao;

import jakarta.persistence.*;
import persistence.model.Account;
import persistence.model.Role;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountDAO implements IDAO<Account> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static AccountDAO instance;
    private static EntityManagerFactory emf;

    public static AccountDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AccountDAO();
        }
        return instance;
    }

    @Override
    public List<Account> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a", Account.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No accounts found.");
        }
    }

    @Override
    public Account getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a WHERE a.id = :id", Account.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No account found with the following id: " + id);
        }
    }

    @Override
    public Account create(Account account) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Check if the role exists
            Role role = em.find(Role.class, account.getRole().getName());

            // If the role doesn't exist, persist it
            if (role == null) {
                role = new Role(account.getRole().getName());
                em.persist(role);
            }

            // Set the role for the account
            account.setRole(role);

            // Persist the account
            em.persist(account);

            em.getTransaction().commit();
            return account;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting account.");
        }
    }

    @Override
    public Account update(Account account) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(account);
            em.getTransaction().commit();
            return account;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating account.");
        }
    }

    @Override
    public Account delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Account account = em.find(Account.class, id);
            if (account != null) {
                em.remove(account);
                em.getTransaction().commit();
            }
            return account;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error deleting account.");
        }
    }
}
