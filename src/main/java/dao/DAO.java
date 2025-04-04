package dao;

import jakarta.persistence.*;
import persistence.model.*;
import utility.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class DAO<T> implements IDAO<T> {

    protected static EntityManagerFactory emf;
    protected static String timestamp = DateUtil.getTimestamp();// = dateFormat.format(new Date());
    //private static SimpleDateFormat dateFormat = DateUtil.getDateFormat();// = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final Class<T> entityClass;

    protected DAO(Class<T> entityClass) {
       // this.dateFormat = dateFormat;
      //  timestamp = dateFormat.format(new Date());
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
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", account.getRole().getName())
                        .getSingleResult();
                account.setRole(role);

            } if (entity instanceof Footer footer) {
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", footer.getRole().getName())
                        .getSingleResult();
                footer.setRole(role);
            } else if (entity instanceof Game game) {
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", game.getAccount().getRole().getName())
                        .getSingleResult();

                Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                        .setParameter("name", game.getAccount().getUsername())
                        .getSingleResult();

                account.setRole(role);
                game.setAccount(account);
            } else if (entity instanceof Header header){
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", header.getRole().getName())
                        .getSingleResult();

                header.setRole(role);
            } else if (entity instanceof Information information){
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", information.getAccount().getRole().getName())
                        .getSingleResult();

                Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                        .setParameter("name", information.getAccount().getUsername())
                        .getSingleResult();

                account.setRole(role);
                information.setAccount(account);

            } else if (entity instanceof License license){
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", license.getGame().getAccount().getRole().getName())
                        .getSingleResult();

                Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                        .setParameter("name", license.getGame().getAccount().getUsername())
                        .getSingleResult();

                Game game = em.createQuery("SELECT r FROM Game r WHERE r.name = :name", Game.class)
                        .setParameter("name", license.getGame().getName())
                        .getSingleResult();

                account.setRole(role);
                game.setAccount(account);
                license.setGame(game);
            } else if (entity instanceof QA qa) {
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", qa.getAccount().getRole().getName())
                        .getSingleResult();

                Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                        .setParameter("name", qa.getAccount().getUsername())
                        .getSingleResult();

                account.setRole(role);
                qa.setAccount(account);
            } else if (entity instanceof Todo todo) {
                Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                        .setParameter("name", todo.getAccount().getRole().getName())
                        .getSingleResult();

                Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                        .setParameter("name", todo.getAccount().getUsername())
                        .getSingleResult();

                account.setRole(role);
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
                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", account.getRole().getName())
                            .getSingleResult();

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
                } else if (entity instanceof Footer footer){
                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", footer.getRole().getName())
                            .getSingleResult();

                    Footer existingFooter = em.find(Footer.class, footer.getId());

                    if (footer.getHeader() == null) {
                        footer.setHeader(existingFooter.getHeader());
                    }
                    if (footer.getDescription() == null) {
                        footer.setDescription(existingFooter.getDescription());
                    }
                    footer.setRole(role);
                } else if (entity instanceof Game game) {
                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", game.getAccount().getRole().getName())
                            .getSingleResult();

                    Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                            .setParameter("name", game.getAccount().getUsername())
                            .getSingleResult();

                    Game existingGame = em.find(Game.class, game.getId());

                    if (game.getName() == null){
                        game.setName(existingGame.getName());
                    }

                    account.setRole(role);
                    game.setAccount(account);
                } else if (entity instanceof Header header) {
                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", header.getRole().getName())
                            .getSingleResult();

                    Header existingHeader = em.find(Header.class, header.getId());

                    if (header.getText() == null){
                        header.setText(existingHeader.getText());
                    }
                    header.setRole(role);
                } else if (entity instanceof Information information) {

                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", information.getAccount().getRole().getName())
                            .getSingleResult();

                    Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                            .setParameter("name", information.getAccount().getUsername())
                            .getSingleResult();

                    Information existingInformation = em.find(Information.class, information.getId());

                    if (information.getDescription() == null){
                        information.setDescription(existingInformation.getDescription());
                    }
                    account.setRole(role);
                    information.setAccount(account);
                } else if (entity instanceof QA qa) {

                    Role role = em.createQuery(
                                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", qa.getAccount().getRole().getName())
                            .getSingleResult();

                    Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                            .setParameter("name", qa.getAccount().getUsername())
                            .getSingleResult();

                    QA existingQa = em.find(QA.class, qa.getId());

                    if (qa.getQuestion() == null){
                        qa.setQuestion(existingQa.getQuestion());
                    }

                    if (qa.getAnswer() == null){
                        qa.setAnswer(existingQa.getAnswer());
                    }

                    account.setRole(role);
                    qa.setAccount(account);
                } else if (entity instanceof  License license){
                    Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", license.getGame().getAccount().getRole().getName())
                            .getSingleResult();

                    Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                            .setParameter("name", license.getGame().getAccount().getUsername())
                            .getSingleResult();

                    Game game = em.createQuery("SELECT r FROM Game r WHERE r.name = :name", Game.class)
                            .setParameter("name", license.getGame().getName())
                            .getSingleResult();

                    account.setRole(role);
                    game.setAccount(account);
                    license.setGame(game);

                    License existingLicense = em.find(License.class, license.getId());

                    if (license.getUsername() == null){
                        license.setUsername(existingLicense.getUsername());
                    }
                    if (license.getPassword() == null){
                        license.setPassword(existingLicense.getPassword());

                    }
                    if (license.getEmail() == null){
                        license.setEmail(existingLicense.getEmail());
                    }
                    if (license.getPcNumber() != existingLicense.getPcNumber()){
                        license.setPcNumber(existingLicense.getPcNumber());
                    }
                } else if (entity instanceof Role role ){
                    Role existingRole = em.find(Role.class, role.getId());

                    if (role.getHeaders() == null){
                        role.setHeaders(existingRole.getHeaders());
                    }
                    if (role.getFooters() == null){
                        role.setFooters(existingRole.getFooters());
                    }
                } else if (entity instanceof Todo todo ){
                    Todo existingTodo = em.find(Todo.class, todo.getId());

                    Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                            .setParameter("name", todo.getAccount().getRole().getName())
                            .getSingleResult();

                    Account account = em.createQuery("SELECT r FROM Account r WHERE r.username = :name", Account.class)
                            .setParameter("name", todo.getAccount().getUsername())
                            .getSingleResult();

                    account.setRole(role);
                    todo.setAccount(account);

                    if (todo.getDescription() == null){
                        todo.setDescription(existingTodo.getDescription());
                    }
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
                    //qa.setAccount(null);
                } else if (entity instanceof Todo todo) {
                    todo.getAccount().getTodos().remove(todo);
                } else if (entity instanceof License license) {
                    license.getGame().getLicenses().remove(license);
                } else if (entity instanceof Todo todo) {
                    todo.setAccount(null);
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

