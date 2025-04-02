package dao;

import jakarta.persistence.*;
import persistence.model.Account;
import persistence.model.Game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameDAO implements IDAO<Game> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static String timestamp = dateFormat.format(new Date());

    private static GameDAO instance;
    private static EntityManagerFactory emf;

    public static GameDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GameDAO();
        }
        return instance;
    }

    @Override
    public List<Game> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Game> query = em.createQuery("SELECT g FROM Game g", Game.class);
            return query.getResultList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No games found.");
        }
    }

    @Override
    public Game getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Game> query = em.createQuery("SELECT g FROM Game g WHERE g.id = :id", Game.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(timestamp + ": " + "No games found with the following id: " + id);
        }
    }

    @Override
    public Game create(Game game) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(game);
            em.getTransaction().commit();
            return game;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error persisting game.");
        }
    }

    @Override
    public Game update(Game game) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(game);
            em.getTransaction().commit();
            return game;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error updating game.");
        }
    }

    @Override
    public Game delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Game game = em.find(Game.class, id);
            Account account = em.find(Account.class, game.getAccount().getId());
            account.removeGame(game);
            if (game != null) {
                em.remove(game);
                em.getTransaction().commit();
            }
            return game;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": " + "Error deleting game. ");
        }
    }
}
