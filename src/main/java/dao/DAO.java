package dao;

import jakarta.persistence.*;
import persistence.model.*;
import utility.DateUtil;

import java.util.List;
import java.util.Objects;

public abstract class DAO<T> implements IDAO<T> {

    protected static EntityManagerFactory emf;
    protected static String timestamp = DateUtil.getTimestamp();
    private final Class<T> entityClass;

    protected DAO(Class<T> entityClass) {
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
            if (entity instanceof Account account) {
                Role role = getRoleByName(em, account.getRole().getName());
                account.setRole(role);
            } else if (entity instanceof Footer footer) {
                Role role = getRoleByName(em, footer.getRole().getName());
                footer.setRole(role);
            } else if (entity instanceof Game game) {
                Account account = attachAccountWithRole(em, game.getAccount());
                game.setAccount(account);
            } else if (entity instanceof Header header) {
                Role role = getRoleByName(em, header.getRole().getName());
                header.setRole(role);
            } else if (entity instanceof Information information) {
                Account account = attachAccountWithRole(em, information.getAccount());
                information.setAccount(account);
            } else if (entity instanceof License license) {
                Account account = attachAccountWithRole(em, license.getGame().getAccount());
                Game game = getGameByName(em, license.getGame().getName());
                game.setAccount(account);
                license.setGame(game);
            } else if (entity instanceof QA qa) {
                Account account = attachAccountWithRole(em, qa.getAccount());
                qa.setAccount(account);
            } else if (entity instanceof Todo todo) {
                Account account = attachAccountWithRole(em, todo.getAccount());
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

    private Role getRoleByName(EntityManager em, Role.RoleName roleName) {
        return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", roleName)
                .getSingleResult();
    }

    private Account getAccountByUsername(EntityManager em, String username) {
        return em.createQuery("SELECT a FROM Account a WHERE a.username = :name", Account.class)
                .setParameter("name", username)
                .getSingleResult();
    }

    private Game getGameByName(EntityManager em, String gameName) {
        return em.createQuery("SELECT g FROM Game g WHERE g.name = :name", Game.class)
                .setParameter("name", gameName)
                .getSingleResult();
    }

    private Account attachAccountWithRole(EntityManager em, Account sourceAccount) {
        Role role = getRoleByName(em, sourceAccount.getRole().getName());
        Account account = getAccountByUsername(em, sourceAccount.getUsername());
        account.setRole(role);
        return account;
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
            if (entity != null) {
                if (entity instanceof Account account) {
                    Role role = getRoleByName(em, account.getRole().getName());
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
                    Role role = getRoleByName(em, footer.getRole().getName());
                    Footer existingFooter = em.find(Footer.class, footer.getId());
                    if (footer.getHeader() == null) {
                        footer.setHeader(existingFooter.getHeader());
                    }
                    if (footer.getDescription() == null) {
                        footer.setDescription(existingFooter.getDescription());
                    }
                    footer.setRole(role);
                } else if (entity instanceof Game game) {
                    Account account = attachAccountWithRole(em, game.getAccount());
                    Game existingGame = em.find(Game.class, game.getId());
                    if (game.getName() == null) {
                        game.setName(existingGame.getName());
                    }
                    game.setAccount(account);
                } else if (entity instanceof Header header) {
                    Role role = getRoleByName(em, header.getRole().getName());
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
                    Game game = getGameByName(em, license.getGame().getName());
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
                    Role existingRole = em.find(Role.class, role.getId());
                    if (role.getHeaders() == null) {
                        role.setHeaders(existingRole.getHeaders());
                    }
                    if (role.getFooters() == null) {
                        role.setFooters(existingRole.getFooters());
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
                } else if (entity instanceof Todo todo) {
                    todo.getAccount().getTodos().remove(todo);
                } else if (entity instanceof License license) {
                    license.getGame().getLicenses().remove(license);
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

