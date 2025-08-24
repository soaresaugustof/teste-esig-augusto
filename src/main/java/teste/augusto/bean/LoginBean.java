package teste.augusto.bean;

import teste.augusto.dao.UsuarioDAO;
import teste.augusto.model.Usuario;
import teste.augusto.utils.FacesUtil;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private UsuarioDAO usuarioDAO;

    private String login;
    private String senha;
    private String confirmarSenha;
    private Usuario usuarioLogado;

    public String efetuarLogin() {
        Usuario usuarioEncontrado = usuarioDAO.buscarPorLogin(this.login);

        if (usuarioEncontrado != null && usuarioEncontrado.verificarSenha(this.senha)) {
            this.usuarioLogado = usuarioEncontrado;
            FacesUtil.mensagemDeInfo("Login efetuado", "Bem-vindo, " + usuarioLogado.getNome() + "!");
            return "/index.xhtml?faces-redirect=true";
        } else {
            FacesUtil.mensagemDeErro("Erro de Login", "Usuário ou senha inválidos.");
            this.login = null;
            this.senha = null;
            return null;
        }
    }

    public String registrar() {
        if (senha == null || senha.isEmpty() || !senha.equals(confirmarSenha)) {
            FacesUtil.mensagemDeErro("Erro de Validação", "As senhas não coincidem ou são inválidas.");
            return null;
        }

        if (usuarioDAO.buscarPorLogin(login) != null) {
            FacesUtil.mensagemDeErro("Erro de Registro", "O login '" + login + "' já existe.");
            return null;
        }

        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setLogin(login);
            novoUsuario.setSenha(senha);
            novoUsuario.setNome("Novo Usuário");
            novoUsuario.setAdmin(true);

            usuarioDAO.save(novoUsuario);
            FacesUtil.mensagemDeInfo("Registro Concluído!", "A sua conta foi criada. Efetue o login.");
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.mensagemDeErro("Erro", "Não foi possível criar o usuário.");
            return null;
        }
    }

    public String efetuarLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public boolean isLogado() {
        return this.usuarioLogado != null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
