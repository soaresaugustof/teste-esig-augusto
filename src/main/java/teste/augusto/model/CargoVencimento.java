package teste.augusto.model;

import javax.persistence.*;

@Entity
@Table(name = "cargo_vencimento")
public class CargoVencimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "vencimento_id")
    private Vencimento vencimento;

    public CargoVencimento(Cargo cargo, Vencimento vencimento) {
        this.cargo = cargo;
        this.vencimento = vencimento;
    }

    public CargoVencimento() {
    }

    public Integer getId() {
        return id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Vencimento getVencimento() {
        return vencimento;
    }

    public void setVencimento(Vencimento vencimento) {
        this.vencimento = vencimento;
    }
}
