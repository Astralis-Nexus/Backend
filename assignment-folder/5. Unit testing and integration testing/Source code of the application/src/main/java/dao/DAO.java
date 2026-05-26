package dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import persistence.model.*;
import utility.DateUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class DAO<T> implements IDAO<T> {

    protected static EntityManagerFactory emf;
    protected static String timestamp = DateUtil.getTimestamp();
    private final Class<T> entityClass;

    protected DAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<T> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root<T> root = criteriaQuery.from(entityClass);
            criteriaQuery.select(root);
            TypedQuery<T> query = em.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(timestamp + ": No " + entityClass.getSimpleName() + " entities found.");
        }
    }

    @Override
    public T create(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            if (entity instanceof Account account) {
                Role.RoleName roleName = account.getRole() != null ? account.getRole().getName() : Role.RoleName.REGULAR;
                Role role = getRoleByName(roleName);
                account.setRole(role);
            } else if (entity instanceof Footer footer) {
                Role role = getRoleByName(footer.getRole().getName());
                footer.setRole(role);
            } else if (entity instanceof Game game) {
                Account account = attachAccountWithRole(em, game.getAccount());
                game.setAccount(account);
            } else if (entity instanceof Header header) {
                Role role = getRoleByName(header.getRole().getName());
                header.setRole(role);
            } else if (entity instanceof Information information) {
                Account account = attachAccountWithRole(em, information.getAccount());
                information.setAccount(account);
            } else if (entity instanceof License license) {
                Account account = attachAccountWithRole(em, license.getGame().getAccount());
                Game game = getGameByName(license.getGame().getName());
                game.setAccount(account);
                license.setGame(game);
            } else if (entity instanceof QA qa) {
                Account account = attachAccountWithRole(em, qa.getAccount());
                qa.setAccount(account);
            } else if (entity instanceof Todo todo) {
                Account account = em.find(Account.class, todo.getAccount().getId());
                todo.setAccount(account);
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

    public Role getRoleByName(Role.RoleName roleName) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", roleName)
                    .getSingleResult();
        }
    }

    public Account getAccountByUsername( String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT a FROM Account a WHERE a.username = :name", Account.class)
                    .setParameter("name", username)
                    .getSingleResult();
        }
    }

    public Game getGameByName(String gameName) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT g FROM Game g WHERE g.name = :name", Game.class)
                    .setParameter("name", gameName)
                    .getSingleResult();
        }
    }

    private Account attachAccountWithRole(EntityManager em, Account sourceAccount) {
        if (sourceAccount == null) {
            throw new IllegalArgumentException("Source account cannot be null");
        }

        // Try to find existing account
        Account existingAccount = getAccountByUsername(sourceAccount.getUsername());
        if (existingAccount == null) {
            throw new RuntimeException("Account with username '" + sourceAccount.getUsername() + "' not found.");
        }

        // Fetch and set role properly
        Role existingRole = getRoleByName(sourceAccount.getRole().getName());
        existingAccount.setRole(existingRole);

        return existingAccount;
    }


    @Override
    public T getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            T entity = em.find(entityClass, id);
            if (entity == null) {
                throw new EntityNotFoundException(timestamp + ": No " + entityClass.getSimpleName() + " found with id: " + id);
            }
            return entity;
        }
    }

    @Override
    public T update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            if (entity != null) {
                if (entity instanceof Account account) {
                    Role role = getRoleByName(account.getRole().getName());
                    Account existingAccount = em.find(Account.class, account.getId());

                    if (account.getUsername() == null) {
                        account.setUsername(existingAccount.getUsername());
                    }
                    if (account.getPassword() == null) {
                        account.setPassword(existingAccount.getPassword());
                    }
                    account.setQas(existingAccount.getQas());
                    account.setGames(existingAccount.getGames());
                    account.setInformations(existingAccount.getInformations());
                    account.setTodos(existingAccount.getTodos());
                    account.setRole(role);
                } else if (entity instanceof Footer footer) {
                    // Get the role by its name
                    //Role role = getRoleByName(footer.getRole().getName());

                    Footer existingFooter = em.find(Footer.class, footer.getId());

                    if (existingFooter != null) {
                        if (footer.getHeader() != null) {
                            existingFooter.setHeader(footer.getHeader());
                        }
                        if (footer.getDescription() != null) {
                            existingFooter.setDescription(footer.getDescription());
                        }
                        if (footer.getRole() != null) {
                            Role role = getRoleByName(footer.getRole().getName());
                            existingFooter.setRole(role);
                        }
                        entity = entityClass.cast(existingFooter);
                    }

                } else if (entity instanceof Game game) {
                    Account account = attachAccountWithRole(em, game.getAccount());
                    Game existingGame = em.find(Game.class, game.getId());
                    if (game.getName() == null) {
                        game.setName(existingGame.getName());
                    }
                    game.setAccount(account);
                } else if (entity instanceof Header header) {
                    Role role = getRoleByName(header.getRole().getName());
                    Header existingHeader = em.find(Header.class, header.getId());
                    if (header.getText() == null) {
                        header.setText(existingHeader.getText());
                    }
                    header.setRole(role);
                } else if (entity instanceof Information information) {
                    Account account = attachAccountWithRole(em, information.getAccount());
                    Information existingInformation = em.find(Information.class, information.getId());

                    if (information.getDescription() == null) {
                        information.setDescription(existingInformation.getDescription());
                    }
                    information.setAccount(account);
                } else if (entity instanceof QA qa) {
                    Account account = attachAccountWithRole(em, qa.getAccount());
                    QA existingQa = em.find(QA.class, qa.getId());
                    if (qa.getQuestion() == null) {
                        qa.setQuestion(existingQa.getQuestion());
                    }
                    if (qa.getAnswer() == null) {
                        qa.setAnswer(existingQa.getAnswer());
                    }
                    qa.setAccount(account);
                } else if (entity instanceof License license) {
                    Account account = attachAccountWithRole(em, license.getGame().getAccount());
                    Game game = getGameByName(license.getGame().getName());
                    game.setAccount(account);
                    license.setGame(game);
                    License existingLicense = em.find(License.class, license.getId());
                    if (license.getUsername() == null) {
                        license.setUsername(existingLicense.getUsername());
                    }
                    if (license.getPassword() == null) {
                        license.setPassword(existingLicense.getPassword());
                    }
                    if (license.getEmail() == null) {
                        license.setEmail(existingLicense.getEmail());
                    }
                    if (!Objects.equals(license.getPcNumber(), existingLicense.getPcNumber())) {
                        license.setPcNumber(existingLicense.getPcNumber());
                    }
                } else if (entity instanceof Role role) {
                    /*Role existingRole = em.find(Role.class, role.getId());
                    if (role.getHeaders() == null) {
                        role.setHeaders(existingRole.getHeaders());
                    }
                    if (role.getFooters() == null) {
                        role.setFooters(existingRole.getFooters());
                    }*/
                    Role existingRole = em.find(Role.class, role.getId());
                    if (existingRole != null) {
                        // Only update fields that have changed
                        if (role.getHeaders() != null) {
                            existingRole.setHeaders(role.getHeaders());
                        }
                        if (role.getFooters() != null) {
                            existingRole.setFooters(role.getFooters());
                        }
                        // Merge changes
                        em.merge(existingRole);
                    }
                } else if (entity instanceof Todo todo) {
                    Todo existingTodo = em.find(Todo.class, todo.getId());
                    Account account = attachAccountWithRole(em, todo.getAccount());
                    if (todo.getDescription() == null) {
                        todo.setDescription(existingTodo.getDescription());
                    }
                    todo.setAccount(account);
                }
            }
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
                    removeFromCollection(footer.getRole().getFooters(), footer);
                } else if (entity instanceof Game game) {
                    Account account = em.find(Account.class, game.getAccount().getId());
                    removeFromCollection(account.getGames(), game);
                } else if (entity instanceof Role role) {
                    Role noneRole = getRoleByName(Role.RoleName.NONE);
                    List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.role = :role", Account.class)
                            .setParameter("role", role)
                            .getResultList();
                    for (Account account : accounts) {
                        account.setRole(noneRole);
                        em.merge(account);
                    }
                } else if (entity instanceof Header header) {
                    removeFromCollection(header.getRole().getHeaders(), header);
                } else if (entity instanceof Information information) {
                    removeFromCollection(information.getAccount().getInformations(), information);
                } else if (entity instanceof QA qa) {
                    removeFromCollection(qa.getAccount().getQas(), qa);
                } else if (entity instanceof Todo todo) {
                    removeFromCollection(todo.getAccount().getTodos(), todo);
                } else if (entity instanceof License license) {
                    removeFromCollection(license.getGame().getLicenses(), license);
                }
                em.remove(entity);
                em.getTransaction().commit();
            }
            return entity;
        } catch (PersistenceException e) {
            throw new PersistenceException(
                    timestamp + ": Error deleting " + entityClass.getSimpleName() + " with id: " + id
            );
        }
    }

    private <E> void removeFromCollection(Collection<E> collection, E entity) {
        if (collection != null) {
            collection.remove(entity);
        }
    }

}

