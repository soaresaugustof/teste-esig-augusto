package teste.augusto.bean;

import teste.augusto.dao.PessoaSalarioConsolidadoDAO;
import teste.augusto.model.PessoaSalarioConsolidado;
import teste.augusto.service.SalarioService;
import teste.augusto.utils.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PessoaSalarioConsolidadoBean implements Serializable {

    @Inject
    private SalarioService salarioService;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioConsolidadoDAO;

    @Inject
    private Event<Object> calculoSalarioEvent;

    private List<PessoaSalarioConsolidado> pessoas;

    @PostConstruct
    public void init() {
        carregarPessoas();
    }

    public void calcularSalarios() {
        try {
            calculoSalarioEvent.fireAsync("INICIAR_CALCULO");

            FacesUtil.mensagemDeInfo("Processamento Iniciado", "O cálculo dos salários foi iniciado.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro!", "Ocorreu um erro ao calcular os salários.");
        }
    }

    private void carregarPessoas() {
        this.pessoas = pessoaSalarioConsolidadoDAO.buscarTodos();
    }

    public List<PessoaSalarioConsolidado> getPessoas() {
        return pessoas;
    }

}
