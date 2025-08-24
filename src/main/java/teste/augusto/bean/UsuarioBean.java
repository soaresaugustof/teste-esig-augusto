package teste.augusto.bean;

import teste.augusto.dao.UsuarioDAO;
import teste.augusto.model.Usuario;
import teste.augusto.utils.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    @Inject
    private UsuarioDAO usuarioDAO;

    private Usuario usuarioSelecionado;
    private List<Usuario> usuarios;

    private String senhaPura;
    private String confirmarSenha;

    @PostConstruct
    public void init() {
        this.usuarios = usuarioDAO.findAll();
        novoUsuario();
    }

    public void novoUsuario() {
        this.usuarioSelecionado = new Usuario();
        this.senhaPura = null;
        this.confirmarSenha = null;
    }

    public void salvar() {
        try {
            if (senhaPura != null && !senhaPura.isEmpty()) {
                if (!senhaPura.equals(confirmarSenha)) {
                    FacesUtil.mensagemDeErro("Erro de Validação", "As senhas não coincidem.");
                    return;
                }

                usuarioSelecionado.setSenha(senhaPura);
            } else if (usuarioSelecionado.getId() == null) {
                FacesUtil.mensagemDeErro("Erro de Validação", "A senha é obrigatória para novos utilizadores.");
                return;
            }

            if (usuarioSelecionado.getId() == null) {
                usuarioDAO.save(usuarioSelecionado);
                FacesUtil.mensagemDeInfo("Sucesso", "Utilizador criado com sucesso.");
            } else {
                Usuario usuarioDoBanco = usuarioDAO.findById(usuarioSelecionado.getId());
                usuarioDoBanco.setNome(usuarioSelecionado.getNome());
                usuarioDoBanco.setLogin(usuarioSelecionado.getLogin());
                usuarioDoBanco.setAdmin(usuarioSelecionado.isAdmin());
                if (senhaPura != null && !senhaPura.isEmpty()) {
                    usuarioDoBanco.setSenha(senhaPura);
                }
                usuarioDAO.update(usuarioDoBanco);
                FacesUtil.mensagemDeInfo("Sucesso", "Utilizador atualizado com sucesso.");
            }

            this.usuarios = usuarioDAO.findAll();
            novoUsuario();

        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro", "Ocorreu um problema ao salvar o utilizador.");
        }
    }

    public void excluir(Usuario usuario) {
        try {
            usuarioDAO.delete(usuario.getId());
            this.usuarios.remove(usuario);
            FacesUtil.mensagemDeInfo("Sucesso", "Utilizador excluído com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro", "Ocorreu um problema ao excluir o utilizador.");
        }
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
        this.senhaPura = null;
        this.confirmarSenha = null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public String getSenhaPura() {
        return senhaPura;
    }

    public void setSenhaPura(String senhaPura) {
        this.senhaPura = senhaPura;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}