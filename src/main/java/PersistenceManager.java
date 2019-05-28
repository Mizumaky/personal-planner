import JPAobjects.*;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.Callable;

//@Transactional

public final class PersistenceManager {
    private static PersistenceManager instance = null;
    private static EntityManagerFactory emFactory = null;
    private static EntityManager em = null;

    //disable constructor
    private PersistenceManager() {
    }

    public static PersistenceManager getInstance() {
        if (instance == null)
            instance = new PersistenceManager();
        return instance;
    }

    public boolean checkEMF() {
        return emFactory != null && emFactory.isOpen();
    }
    public boolean checkEM() {
        return em != null && em.isOpen();
    }

    public void connect() throws DBErrorException {
        try {
            if (!checkEMF()) {
                emFactory = Persistence.createEntityManagerFactory("TaskiraPersistence");
                System.err.println("INFO[PersistanceManager]: EntityManagerFactory created.");
            } else {
                System.err.println("INFO[PersistanceManager]: The EntityManagerFactory is still open.");
            }
            if (!checkEM()) {
                em = emFactory.createEntityManager();
                System.err.println("INFO[PersistanceManager]: EntityManager created.");
            } else {
                System.err.println("INFO[PersistanceManager]: The EntityManager is still open.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBErrorException();
        }
    }

    public void close() throws DBErrorException {
        try {
            if (checkEM()) {
                em.close();
                System.err.println("INFO[PersistanceManager]: EntityManager closed.");
            } else {
                System.err.println("WARNING[PersistanceManager]: There's no EntityManager to close.");
            }
            if (checkEMF()) {
                emFactory.close();
                System.err.println("INFO[PersistanceManager]: EntityManagerFactory closed.");
            } else {
                System.err.println("WARNING[PersistanceManager]: There's no EntityManagerFactory to close.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBErrorException();
        }
    }
//----------------------------------------------------------------------

    private <T> void writeTransaction(Callable callable) throws DBErrorException {
        try {
            connect();
            em.getTransaction().begin();
            callable.call();
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            throw new DBErrorException();
        }  finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
    private <T> T readTransaction(Callable<T> callable) throws DBErrorException {
        try {
            connect();
            em.getTransaction().begin();
            T ret = callable.call();
            em.getTransaction().commit();
            return ret;
        } catch (Exception e){
            e.printStackTrace();
            throw new DBErrorException();
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }

    public <T> void persist(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.persist(entity);
            return null;
        });
    }
    public <T> void merge(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.merge(entity);
            return null;
        });
    }
    public <T> void remove(T entity) throws DBErrorException {
        writeTransaction((Callable) () -> {
            em.remove(entity);
            return null;
        });
    }

    public List<TaskEntity> fetchAllTasks() throws DBErrorException {
        return readTransaction((Callable<List<TaskEntity>>) () -> {
            TypedQuery<TaskEntity> query = em.createNamedQuery("TaskEntity.findAll", TaskEntity.class);
            return query.getResultList();
        });
    }
    public List<CategoryEntity> fetchRootCategories() throws DBErrorException {
        return readTransaction((Callable<List<CategoryEntity>>) () -> {
            TypedQuery<CategoryEntity> query = em.createNamedQuery("CategoryEntity.findAll", CategoryEntity.class);
            return query.getResultList();
        });
    }
    public List<TagEntity> fetchAllTags() throws DBErrorException {
        return readTransaction((Callable<List<TagEntity>>) () -> {
            TypedQuery<TagEntity> query = em.createQuery("SELECT t from TagEntity as t", TagEntity.class);
            return query.getResultList();
        });
    }


}
