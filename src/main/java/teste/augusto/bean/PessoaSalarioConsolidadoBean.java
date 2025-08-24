package teste.augusto.bean;

import net.sf.jasperreports.engine.JRException;
import teste.augusto.dao.PessoaSalarioConsolidadoDAO;
import teste.augusto.model.PessoaSalarioConsolidado;
import teste.augusto.service.RelatorioService;
import teste.augusto.service.SalarioService;
import teste.augusto.utils.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList; // <-- ADICIONE ESTE IMPORT
import java.util.List;
import java.util.concurrent.ExecutorService;

@Named("pessoaSalarioBean") // Use the correct bean name from your XHTML
@ViewScoped
public class PessoaSalarioConsolidadoBean implements Serializable {

    @Inject
    private SalarioService salarioService;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioDAO;

    @Inject
    private ExecutorService executor;

    @Inject
    private RelatorioService relatorioService;

    private List<PessoaSalarioConsolidado> pessoas = new ArrayList<>();

    @PostConstruct
    public void init() {
        carregarPessoas();
    }

    public void calcularSalarios() {
        try {
            Runnable tarefaDeCalculo = () -> {
                try {
                    System.out.println("==> Tarefa assíncrona de cálculo de salários iniciada na thread: " + Thread.currentThread().getName());
                    salarioService.calcularEPreencherSalarios();
                    System.out.println("==> Tarefa assíncrona de cálculo de salários concluída com sucesso.");
                } catch (Exception e) {
                    System.err.println("==> ERRO GRAVE na tarefa assíncrona de cálculo:");
                    e.printStackTrace();
                }
            };
            executor.submit(tarefaDeCalculo);
            FacesUtil.mensagemDeInfo("Processamento Iniciado", "O cálculo dos salários foi iniciado em segundo plano. A tabela será atualizada em breve.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro!", "Não foi possível iniciar o processo de cálculo.");
        }
    }

    public void carregarPessoas() {
        this.pessoas = pessoaSalarioDAO.buscarTodos();
    }

    public void gerarRelatorio() {
        try {
            byte[] relatorioPdf = relatorioService.gerarRelatorioPdf(this.pessoas);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_pessoas.pdf\"");
            response.setContentLength(relatorioPdf.length);

            OutputStream os = response.getOutputStream();
            os.write(relatorioPdf);
            os.flush();

            facesContext.responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro", "Não foi possível gerar o relatório: " + e.getMessage());
        }
    }

    public List<PessoaSalarioConsolidado> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<PessoaSalarioConsolidado> pessoas) {
        this.pessoas = pessoas;
    }
}