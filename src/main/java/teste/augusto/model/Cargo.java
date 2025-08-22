package teste.augusto.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<Pessoa> pessoas;

    @OneToMany(mappedBy = "cargo", fetch = FetchType.LAZY)
    private List<CargoVencimento> vencimentos;

    public Cargo() {}

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public List<CargoVencimento> getVencimentos() {
        return vencimentos;
    }

    public void setVencimentos(List<CargoVencimento> vencimentos) {
        this.vencimentos = vencimentos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
