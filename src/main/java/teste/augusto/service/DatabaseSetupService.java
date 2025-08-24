package teste.augusto.service;

import teste.augusto.utils.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@ApplicationScoped
public class DatabaseSetupService {

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object event) {
        System.out.println("==> APLICAÇÃO INICIADA: A verificar e sincronizar as sequences do banco de dados...");
        synchronizeAllSequences();
        System.out.println("==> Sincronização de sequences concluída.");
    }

    private void synchronizeAllSequences() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            synchronizeSequence(em, "pessoa_id_seq", "pessoa");
            synchronizeSequence(em, "cargo_id_seq", "cargo");
            synchronizeSequence(em, "vencimentos_id_seq", "vencimentos");
            synchronizeSequence(em, "cargo_vencimentos_id_seq", "cargo_vencimentos");
            synchronizeSequence(em, "usuario_id_seq", "usuario");

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("ERRO GRAVE ao sincronizar as sequences: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    private void synchronizeSequence(EntityManager em, String sequenceName, String tableName) {
        try {
            String sql = "SELECT setval('" + sequenceName + "', (SELECT COALESCE(MAX(id), 1) FROM " + tableName + "))";
            Query query = em.createNativeQuery(sql);
            query.getSingleResult();
            System.out.println(" -> Sequence '" + sequenceName + "' sincronizada com sucesso.");
        } catch (Exception e) {
            System.err.println("  -> Falha ao sincronizar a sequence '" + sequenceName + "'. Verifique se o nome está correto. Erro: " + e.getMessage());
        }
    }
}