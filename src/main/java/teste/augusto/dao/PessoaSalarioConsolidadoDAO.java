package teste.augusto.dao;

import teste.augusto.model.PessoaSalarioConsolidado;
import teste.augusto.utils.JPAUtil;

import javax.faces.bean.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PessoaSalarioConsolidadoDAO extends DAO<PessoaSalarioConsolidado> {

    /**
     * Método para realizar a busca de todos os registros
     * da tabela PessoaSalarioConsolidado, ordenado por nome.
     * @return
     */
    public List<PessoaSalarioConsolidado> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager(); // Obtém seu próprio EM
        try {
            String jpql = "SELECT p FROM PessoaSalarioConsolidado p ORDER BY p.nome";
            return em.createQuery(jpql, PessoaSalarioConsolidado.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Método que faz a exclusão dos registros da
     * tabela PessoaSalarioConsolidado para fazer a adição dos novos registros.
     * @param lista
     */
    public void limparEInserirTodos(List<PessoaSalarioConsolidado> lista) {
        EntityManager em = JPAUtil.getEntityManager(); // Obtém seu próprio EM
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM PessoaSalarioConsolidado").executeUpdate();

            for (PessoaSalarioConsolidado psc : lista) {
                em.persist(psc);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar salários consolidados em lote", e);
        } finally {
            em.close();
        }
    }
}
