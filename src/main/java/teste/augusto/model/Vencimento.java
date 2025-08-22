package teste.augusto.model;

import teste.augusto.enums.TipoVencimento;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vencimento")
public class Vencimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoVencimento tipo;

    public Vencimento() {}

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoVencimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoVencimento tipo) {
        this.tipo = tipo;
    }
}
