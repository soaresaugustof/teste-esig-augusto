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

    /**
     * Define a senha do utilizador, armazenando o seu hash SHA-256.
     * @param senhaPura A senha em texto puro a ser codificada.
     */
    public void setSenha(String senhaPura) {
        if (senhaPura != null && !senhaPura.isEmpty()) {
            this.senha = hashSenha(senhaPura);
        }
    }

    /**
     * Verifica se a senha fornecida corresponde à senha armazenada.
     * @param senhaPura A senha em texto puro para verificação.
     * @return true se a senha corresponder, false caso contrário.
     */
    public boolean verificarSenha(String senhaPura) {
        if (senhaPura == null || this.senha == null) {
            return false;
        }
        return this.senha.equals(hashSenha(senhaPura));
    }

    /**
     * Gera um hash SHA-256 da senha.
     * Numa aplicação de produção, bibliotecas como BCrypt ou Argon2 são mais recomendadas.
     * @param senhaPura A senha em texto puro.
     * @return A senha codificada em Base64.
     */
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
