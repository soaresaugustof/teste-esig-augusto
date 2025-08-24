package teste.augusto.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import teste.augusto.model.PessoaSalarioConsolidado;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RelatorioService implements Serializable {


    public byte[] gerarRelatorioPdf(List<PessoaSalarioConsolidado> dados) throws JRException {

        String caminhoRelatorio = "/reports/relatorio_pessoas.jrxml";
        InputStream relatorioStream = getClass().getResourceAsStream(caminhoRelatorio);

        if (relatorioStream == null) {
            throw new JRException("Recurso do relatório não encontrado em: " + caminhoRelatorio);
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO_RELATORIO", "Relatório de Salários Consolidados");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}