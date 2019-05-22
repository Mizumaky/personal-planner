import JPAobjects.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;

//@Transactional

public class PersistenceManager {
    private static EntityManagerFactory emFactory = null;
    private static EntityManager em = null;

    //disable constructor
    private PersistenceManager() {}

    public static boolean start() {
        try {
            if (emFactory == null) {
                emFactory = Persistence.createEntityManagerFactory("TaskiraPersistence");
                System.err.println("INFO[PersistanceManager]: EntityManagerFactory created.");
                em = emFactory.createEntityManager();
                System.err.println("INFO[PersistanceManager]: EntityManager created.");
            } else {
                System.err.println("WARNING[PersistanceManager]: EntityManagerFactory was already created, did nothing now.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean refresh() {
        try {
            if (emFactory == null || !emFactory.isOpen()) {
                emFactory = Persistence.createEntityManagerFactory("TaskiraPersistence");
                System.err.println("INFO[PersistanceManager]: EntityManagerFactory created.");
            } else {
                System.err.println("INFO[PersistanceManager]: The EntityManagerFactory is still open.");
            }
            if (em == null || !em.isOpen()) {
                em = emFactory.createEntityManager();
                System.err.println("INFO[PersistanceManager]: EntityManager created.");
            } else {
                System.err.println("INFO[PersistanceManager]: The EntityManager is still open.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void close() {
        if (em != null && em.isOpen()) {
            em.close();
            System.err.println("INFO[PersistanceManager]: EntityManager closed.");
        } else {
            System.err.println("WARNING[PersistanceManager]: There's no EntityManager to close.");
        }
        if (emFactory != null && emFactory.isOpen()) {
            emFactory.close();
            System.err.println("INFO[PersistanceManager]: EntityManagerFactory closed.");
        } else {
            System.err.println("WARNING[PersistanceManager]: There's no EntityManagerFactory to close.");
        }
    }
//----------------------------------------------------------------------

    public static <T> boolean persist(T entity) {
        try {
            em.getTransaction().begin();

            em.persist(entity);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }
    public static <T> boolean merge(T entity) {
        try {
            em.getTransaction().begin();

            em.merge(entity);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }
    public static <T> boolean remove(T entity) {
        try {
            em.getTransaction().begin();

            em.remove(entity);

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }
    public static List<TaskEntity> fetchAllTasks() {
        try {
            em.getTransaction().begin();

            TypedQuery<TaskEntity> query = em.createNamedQuery("TaskEntity.findAll", TaskEntity.class);
            List<TaskEntity> tasks = query.getResultList();

            em.getTransaction().commit();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
    }
    public static List<TagEntity> fetchAllTags() {
        try {
            em.getTransaction().begin();

            TypedQuery<TagEntity> query = em.createQuery("SELECT t from TagEntity as t", TagEntity.class);
            List<TagEntity> tags = query.getResultList();

            em.getTransaction().commit();
            return tags;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
    }
    //public static

}
