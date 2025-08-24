package teste.augusto.dao;

import teste.augusto.utils.JPAUtil;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class DAO<T> implements Serializable {
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public DAO() {
        Type genericSuperclass = getClass().getGenericSuperclass();

        while (!(genericSuperclass instanceof ParameterizedType)) {
            if (genericSuperclass instanceof Class) {
                genericSuperclass = ((Class<?>) genericSuperclass).getGenericSuperclass();
            } else {
                throw new IllegalStateException("Não foi possível encontrar o tipo parametrizado na hierarquia de classes.");
            }
        }

        ParameterizedType type = (ParameterizedType) genericSuperclass;
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    public void save(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Falha ao salvar a entidade.", e);
        } finally {
            em.close();
        }
    }

    public T update(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T mergedEntity = em.merge(entity);
            em.getTransaction().commit();
            return mergedEntity;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Falha ao atualizar a entidade.", e);
        } finally {
            em.close();
        }
    }

    public void delete(Object id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Falha ao deletar a entidade.", e);
        } finally {
            em.close();
        }
    }

    public T findById(Object id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } finally {
            em.close();
        }
    }
}
