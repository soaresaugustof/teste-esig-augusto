package teste.augusto.dao;

import teste.augusto.model.Pessoa;
import teste.augusto.utils.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PessoaDAO extends DAO<Pessoa> implements Serializable {

    public Pessoa buscarPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Pessoa p WHERE p.email = :email";
            TypedQuery<Pessoa> query = em.createQuery(jpql, Pessoa.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
