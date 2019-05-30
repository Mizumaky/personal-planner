import JPAobjects.*;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton class for managing the connection with the database using a persistence entity manager.
 * It contains methods for accessing the database. Connect method needs to be called first for entity manager initialization.
 * The methods can throw a custom database connection error if an exception during DB access occurs.
 */
public final class PersistenceManager {
    private static final Logger LOGGER = Logger.getLogger(PersistenceManager.class.getName());

    private static PersistenceManager instance = null;
    private EntityManagerFactory emFactory = null;
    private EntityManager em = null;

    //disable constructor
    private PersistenceManager() {}

    /**
     * Gets the instance of this class.
     *
     * @return the instance
     */
    public static PersistenceManager getInstance() {
        if (instance == null)
            instance = new PersistenceManager();
        return instance;
    }

    /**
     * Returns true if entity manager factory exists and is open.
     *
     * @return the boolean
     */
    public boolean checkEMF() {
        return emFactory != null && emFactory.isOpen();
    }

    /**
     * Returns true if entity manager exists and is open.
     *
     * @return the boolean
     */
    public boolean checkEM() {
        return em != null && em.isOpen();
    }

    /**
     * Initializes new entity manager factory and/or entity manager if needed, which create a connection with the database.
     *
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public void connect() throws DBErrorException {
        try {
            if (!checkEMF()) {
                emFactory = Persistence.createEntityManagerFactory("TaskiraPersistence");
                LOGGER.info("Entity manager factory instantiated - connection established");
            }
            if (!checkEM()) {
                em = emFactory.createEntityManager();
                LOGGER.info("Entity manager instantiated");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Persistance manager caught an error", e);
            throw new DBErrorException();
        }
    }

//    public void close() throws DBErrorException {
//        try {
//            if (checkEM()) {
//                em.close();
//                System.err.println("INFO[PersistanceManager]: EntityManager closed.");
//            } else {
//                System.err.println("WARNING[PersistanceManager]: There's no EntityManager to close.");
//            }
//            if (checkEMF()) {
//                emFactory.close();
//                System.err.println("INFO[PersistanceManager]: EntityManagerFactory closed.");
//            } else {
//                System.err.println("WARNING[PersistanceManager]: There's no EntityManagerFactory to close.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new DBErrorException();
//        }
//    }

    /**
     * An internal method for wrapping a write DB command in a transaction and a try catch.
     * @param callable a method to be wrapped in this method
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    private void writeTransaction(Callable callable) throws DBErrorException {
        try {
            connect();
            em.getTransaction().begin();
            callable.call();
            em.getTransaction().commit();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Persistance manager error", e);
            throw new DBErrorException();
        } //finally {
//            if (em.getTransaction().isActive())
//                em.getTransaction().rollback();
//        }
    }

    /**
     * An internal method for wrapping a read DB command in a transaction and a try catch.
     * @param callable a method to be wrapped in this method
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    private <T> T readTransaction(Callable<T> callable) throws DBErrorException {
        try {
            connect();
            em.getTransaction().begin();
            T ret = callable.call();
            em.getTransaction().commit();
            return ret;
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Persistance manager error", e);
            throw new DBErrorException();
        } //finally {
//            if (em.getTransaction().isActive())
//                em.getTransaction().rollback();
//        }
    }

    /**
     * Persist an entity.
     *
     * @param <T>    the entity type
     * @param entity the entity
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public <T> void persist(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.persist(entity);
            return null;
        });
    }

    /**
     * Merge an entity.
     *
     * @param <T>    the entity type
     * @param entity the entity
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public <T> void merge(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.merge(entity);
            return null;
        });
    }

    /**
     * Remove an entity.
     *
     * @param <T>    the entity type
     * @param entity the entity
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public <T> void remove(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.remove(entity);
            return null;
        });
    }

    /**
     * Fetch all tasks list.
     *
     * @return the list
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public List<TaskEntity> fetchAllTasks() throws DBErrorException {
        return readTransaction((Callable<List<TaskEntity>>) () -> {
            TypedQuery<TaskEntity> query = em.createNamedQuery("TaskEntity.findAll", TaskEntity.class);
            return query.getResultList();
        });
    }

    /**
     * Fetch root categories list.
     *
     * @return the list
     * @throws DBErrorException a database access exception error if something goes wrong
     */
    public List<CategoryEntity> fetchRootCategories() throws DBErrorException {
        return readTransaction((Callable<List<CategoryEntity>>) () -> {
            TypedQuery<CategoryEntity> query = em.createNamedQuery("CategoryEntity.findAll", CategoryEntity.class);
            return query.getResultList();
        });
    }
//    public List<TagEntity> fetchAllTags() throws DBErrorException {
//        return readTransaction((Callable<List<TagEntity>>) () -> {
//            TypedQuery<TagEntity> query = em.createQuery("SELECT t from TagEntity as t", TagEntity.class);
//            return query.getResultList();
//        });
//    }


}
