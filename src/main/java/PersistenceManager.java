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

    public static void start() {
        if (emFactory == null) {
            emFactory = Persistence.createEntityManagerFactory("TaskiraPersistence");
            System.err.println("INFO[PersistanceManager]: EntityManagerFactory created.");
            em = emFactory.createEntityManager();
            System.err.println("INFO[PersistanceManager]: EntityManager created.");
        } else {
            System.err.println("WARNING[PersistanceManager]: EntityManagerFactory was already created, did nothing now.");
        }
    }
    public static void refresh() {
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

    public static int addTask(TaskEntity taskEntity) {
        em.getTransaction().begin();

        em.persist(taskEntity);

        em.getTransaction().commit();
        return taskEntity.getId();
    }
    public static List<TaskEntity> getAllTasks() {
        em.getTransaction().begin();

        TypedQuery<TaskEntity> query = em.createQuery("SELECT t from TaskEntity as t", TaskEntity.class);
        List<TaskEntity> tasks = query.getResultList();

        em.getTransaction().commit();
        return tasks;
    }

    public static int addTag(TagEntity tagEntity) {
        em.getTransaction().begin();

        em.persist(tagEntity);

        em.getTransaction().commit();
        return tagEntity.getId();
    }

    public static List<TagEntity> getAllTags() {
        em.getTransaction().begin();

        TypedQuery<TagEntity> query = em.createQuery("SELECT t from TagEntity as t", TagEntity.class);
        List<TagEntity> tags = query.getResultList();

        em.getTransaction().commit();
        return tags;
    }
    //public static

}
