package teste.augusto.model;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(unique = true)
    private String login;

    @Column(name = "senha")
    private String senha;

    @Column(name = "is_admin")
    private boolean isAdmin;

    public void setSenha(String senhaPura) {
        if (senhaPura != null && !senhaPura.isEmpty()) {
            this.senha = hashSenha(senhaPura);
        }
    }

    public boolean verificarSenha(String senhaPura) {
        if (senhaPura == null || this.senha == null) {
            return false;
        }
        return this.senha.equals(hashSenha(senhaPura));
    }

    private String hashSenha(String senhaPura) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senhaPura.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Não foi possível gerar o hash da senha.", e);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
