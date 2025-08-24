package teste.augusto.bean;

import teste.augusto.dao.CargoDAO;
import teste.augusto.dao.PessoaDAO;
import teste.augusto.model.Cargo;
import teste.augusto.model.Pessoa;
import teste.augusto.utils.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PessoaBean implements Serializable {

    @Inject
    private PessoaDAO pessoaDAO;

    @Inject
    private CargoDAO cargoDAO;

    private Pessoa pessoaSelecionada;
    private List<Pessoa> pessoas;
    private List<Cargo> cargosDisponiveis;

    @PostConstruct
    public void init() {
        this.pessoas = pessoaDAO.findAll();
        this.cargosDisponiveis = cargoDAO.findAll();
        novaPessoa();
    }

    public void novaPessoa() {
        this.pessoaSelecionada = new Pessoa();
    }

    public void salvar() {
        try {
            Pessoa pessoaExistente = pessoaDAO.buscarPorEmail(pessoaSelecionada.getEmail().trim());

            if (pessoaSelecionada.getId() == null) {
                if (pessoaExistente != null) {
                    FacesUtil.mensagemDeErro("Erro de Validação", "O e-mail '" + pessoaSelecionada.getEmail() + "' já está em uso.");
                    return;
                }
            }

            else {
                if (pessoaExistente != null && !pessoaExistente.getId().equals(pessoaSelecionada.getId())) {
                    FacesUtil.mensagemDeErro("Erro de Validação", "O e-mail '" + pessoaSelecionada.getEmail() + "' já pertence a outra pessoa.");
                    return;
                }
            }

            if (pessoaSelecionada.getId() == null) {
                pessoaDAO.save(pessoaSelecionada);
                FacesUtil.mensagemDeInfo("Sucesso!", "Pessoa criada com sucesso.");
            } else {
                pessoaDAO.update(pessoaSelecionada);
                FacesUtil.mensagemDeInfo("Sucesso!", "Pessoa atualizada com sucesso.");
            }

            this.pessoas = pessoaDAO.findAll();
            novaPessoa();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro!", "Ocorreu um problema ao salvar a pessoa.");
        }
    }

    public void excluir(Pessoa pessoa) {
        try {
            pessoaDAO.delete(pessoa.getId());
            this.pessoas.remove(pessoa);
        FacesUtil.mensagemDeInfo("Sucesso!", "Pessoa excluida com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro!", "Ocorreu um problema ao excluir a pessoa.");
        }
    }

    public Pessoa getPessoaSelecionada() {
        return pessoaSelecionada;
    }

    public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
        this.pessoaSelecionada = pessoaSelecionada;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public List<Cargo> getCargosDisponiveis() {
        return cargosDisponiveis;
    }
}
