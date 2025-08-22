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
    private String confirmarSenha;

    @PostConstruct
    public void init() {
        this.usuarios = usuarioDAO.findAll();
        novoUsuario();
    }

    public void novoUsuario() {
        this.usuarioSelecionado = new Usuario();
        this.confirmarSenha = null;
    }

    public void salvar() {
        try {
            if (confirmarSenha != null && !confirmarSenha.isEmpty()) {
                if (!confirmarSenha.equals(usuarioSelecionado.getSenha())) {
                    FacesUtil.mensagemDeErro("Erro de Validação", "As senhas não coincidem.");
                    return;
                }
            }

            if (usuarioSelecionado.getId() == null) {
                if (confirmarSenha == null || confirmarSenha.isEmpty()) {
                    FacesUtil.mensagemDeErro("Erro de Validação", "Confirme a senha.");
                    return;
                }
                usuarioDAO.save(usuarioSelecionado);
                FacesUtil.mensagemDeInfo("Sucesso", "Usuario criado com sucesso!");
            } else {
                usuarioDAO.update(usuarioSelecionado);
                FacesUtil.mensagemDeInfo("Sucesso", "Usuario atualizado com sucesso!");
            }

            this.usuarios = usuarioDAO.findAll();
            novoUsuario();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro", "Ocorreu um problema ao salvar o usuário.");
        }
    }

    public void excluir(Usuario usuario) {
        try {
            usuarioDAO.delete(usuario.getId());
            this.usuarios.remove(usuario);
            FacesUtil.mensagemDeInfo("Sucesso", "Utilizador excluído com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeInfo("Erro", "Ocorreu um problema ao excluir o utilizador.");
        }
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
        // Limpa a senha ao selecionar um utilizador para edição, por segurança
        this.confirmarSenha = null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}
