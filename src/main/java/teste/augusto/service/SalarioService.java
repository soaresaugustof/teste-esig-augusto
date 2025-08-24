package teste.augusto.service;

import teste.augusto.dao.CargoDAO;
import teste.augusto.dao.PessoaDAO;
import teste.augusto.dao.PessoaSalarioConsolidadoDAO;
import teste.augusto.enums.TipoVencimento;
import teste.augusto.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class SalarioService  implements Serializable {

    @Inject
    private PessoaDAO pessoaDAO;

    @Inject
    private CargoDAO cargoDAO;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioDAO;

    public void calcularEPreencherSalarios() {
        List<Pessoa> todasAsPessoas = pessoaDAO.findAll();
        List<Cargo> todosOsCargos = cargoDAO.buscarTodosComVencimentos();

        Map<Integer, Cargo> mapaDeCargos = todosOsCargos.stream()
                .collect(Collectors.toMap(Cargo::getId, cargo -> cargo));

        List<PessoaSalarioConsolidado> salariosConsolidados = new ArrayList<>();

        for (Pessoa pessoa : todasAsPessoas) {
            if (pessoa.getCargo() == null) {
                continue;
            }

            Cargo cargoDaPessoa = mapaDeCargos.get(pessoa.getCargo().getId());
            if (cargoDaPessoa == null || cargoDaPessoa.getVencimentos() == null) {
                continue;
            }

            BigDecimal salarioCalculado = BigDecimal.ZERO;

            for (CargoVencimento cv : cargoDaPessoa.getVencimentos()) {
                Vencimento vencimento = cv.getVencimento();
                if (vencimento.getTipo() == TipoVencimento.CREDITO) {
                    salarioCalculado = salarioCalculado.add(vencimento.getValor());
                } else if (vencimento.getTipo() == TipoVencimento.DEBITO) {
                    salarioCalculado = salarioCalculado.subtract(vencimento.getValor());
                }
            }

            PessoaSalarioConsolidado consolidado = new PessoaSalarioConsolidado();
            consolidado.setPessoaId(pessoa.getId());
            consolidado.setNome(pessoa.getNome());
            consolidado.setCargo(cargoDaPessoa.getNome());
            consolidado.setSalario(salarioCalculado);

            salariosConsolidados.add(consolidado);
        }

        pessoaSalarioDAO.limparEInserirTodos(salariosConsolidados);
    }

    public void onCalculoSalarioRequisitado(@ObservesAsync String payload) {
        if (payload.equals("INICIAR_CALCULO")) {
            System.out.println("==> Evento de cálculo de salários recebido. Iniciando processo.");
            try {
                calcularEPreencherSalarios();
                System.out.println("==> Processo de cálculo concluído com sucesso.");
            } catch (Exception e) {
                System.err.println("==> Erro durante o processo assíncrono de cálculo de salários: ");
                e.printStackTrace();
            }
        }
    }
}
