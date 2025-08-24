package teste.augusto.dao;

import teste.augusto.model.Usuario;
import teste.augusto.utils.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;

@ApplicationScoped
public class UsuarioDAO extends DAO<Usuario> implements Serializable {

    public Usuario buscarPorLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.login = :login";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("login", login);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
