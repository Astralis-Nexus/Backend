package dao;

import jakarta.persistence.*;
import persistence.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class DAO<T> implements IDAO<T> {

    protected static EntityManagerFactory emf;
    protected static String timestamp;// = dateFormat.format(new Date());
    private static SimpleDateFormat dateFormat;// = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final Class<T> entityClass;

    protected DAO(Class<T> entityClass, SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        timestamp = dateFormat.format(new Date());
        this.entityClass = entityClass;
    }

    private static <D extends DAO<?>> D getInstance(EntityManagerFactory _emf, Class<D> daoClass) {
        try {
            emf = _emf;
            return daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create DAO instance", e);
        }
    }

    @Override
    public List<T> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<T> query = em.createQuery("SELECT a FROM " + entityClass.getSimpleName() + " a", entityClass);
            return query.getResultList();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(timestamp + ": No " + entityClass.getSimpleName() + " entities found.");
        }
    }

    @Override
    public T create(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Dynamically handle role if present
            try {
                var getRoleMethod = entity.getClass().getMethod("getRole");
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", (Role.RoleName) getRoleMethod.invoke(entity).getClass().getMethod("getName").invoke(getRoleMethod.invoke(entity)))
                        .getSingleResult();

                var setRoleMethod = entity.getClass().getMethod("setRole", Role.class);
                setRoleMethod.invoke(entity, role);
            } catch (NoSuchMethodException ignored) {
                // Entity does not have getRole/setRole methods, skip role handling
            }

            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": Error persisting " + entity + ".");
        } catch (Exception e) {
            throw new RuntimeException("Error handling entity dynamically", e);
        }
    }

    @Override
    public T getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<T> query = em.createQuery("SELECT a FROM " + entityClass.getSimpleName() + " a WHERE a.id = :id", entityClass);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(timestamp + ": No " + entityClass.getSimpleName() + " found with id: " + id);
        }
    }

    @Override
    public T update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            T updatedEntity = em.merge(entity);
            em.getTransaction().commit();
            return updatedEntity;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": Error updating " + entity + ".");
        }
    }

    @Override
    public T delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            T entity = em.find(entityClass, id);

            if (entity != null) {
                if (entity instanceof Footer footer) {
                    footer.getRole().getFooters().remove(footer);
                } else if (entity instanceof Game game) {
                    Account account = em.find(Account.class, game.getAccount().getId());
                    account.getGames().remove(game);
                } else if (entity instanceof Role role) {

                    Role noneRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", Role.RoleName.NONE)
                            .getSingleResult();

                    List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.role = :role", Account.class)
                            .setParameter("role", role)
                            .getResultList();

                    for (Account account : accounts) {
                        account.setRole(noneRole);
                        em.merge(account);
                    }
                } else if (entity instanceof Header header) {
                    header.getRole().getHeaders().remove(header);
                } else if (entity instanceof Information information) {
                    information.getAccount().getInformations().remove(information);
                } else if (entity instanceof QA qa) {
                    qa.getAccount().getQas().remove(qa);
                    //qa.setAccount(null);
                } else if (entity instanceof Todo todo) {
                    todo.getAccount().getTodos().remove(todo);
                }
                em.remove(entity);
                em.getTransaction().commit();
            }
            return entity;
        } catch (PersistenceException e) {
            throw new PersistenceException(timestamp + ": Error deleting " + entityClass.getSimpleName() + " with id: " + id);
        }
    }
}

