package teste.augusto.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pessoa_salario_consolidado")
public class PessoaSalarioConsolidado {
    @Id
    @Column(name = "pessoa_id")
    private Integer pessoaId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "salario")
    private BigDecimal salario;

    public PessoaSalarioConsolidado() {}

    public Integer getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Integer pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}
