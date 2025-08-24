package teste.augusto.dao;

import teste.augusto.model.Cargo;
import teste.augusto.utils.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class CargoDAO extends DAO<Cargo> implements Serializable {

    public List<Cargo> buscarTodosComVencimentos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT c FROM Cargo c LEFT JOIN FETCH c.vencimentos cv LEFT JOIN FETCH cv.vencimento";
            return em.createQuery(jpql, Cargo.class).getResultList();
        } finally {
            em.close();
        }
    }
}
