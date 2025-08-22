package teste.augusto.dao;

import teste.augusto.model.Cargo;
import teste.augusto.utils.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class CargoDAO extends DAO<Cargo> {

    /**
     * Método para buscar todos os cargos com seus vencimentos de uma só vez
     */
    public List<Cargo> buscarTodosComVencimentos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT c FROM Cargo c LEFT JOIN FETCH c.cargoVencimentos cv LEFT JOIN FETCH cv.vencimento";
            return em.createQuery(jpql, Cargo.class).getResultList();
        } finally {
            em.close();
        }
    }
}
