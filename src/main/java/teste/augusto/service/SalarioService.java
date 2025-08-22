package teste.augusto.service;

import teste.augusto.dao.CargoDAO;
import teste.augusto.dao.PessoaDAO;
import teste.augusto.dao.PessoaSalarioConsolidadoDAO;
import teste.augusto.enums.TipoVencimento;
import teste.augusto.model.*;

import javax.enterprise.event.ObservesAsync;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class SalarioService {

    @Inject
    private PessoaDAO pessoaDAO;

    @Inject
    private CargoDAO cargoDAO;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioDAO;

    /**
     * Método que faz a busca dos dados em cada tabela,
     * e itera sobre cada pessoa para calcular o salário.
     */
    public void calcularEPreencherSalarios() {
        List<Pessoa> pessoas = pessoaDAO.findAll();
        List<Cargo> cargos = cargoDAO.buscarTodosComVencimentos();

        Map<Integer, Cargo> mapaDeCargos = cargos.stream()
                .collect(Collectors.toMap(Cargo::getId, Function.identity()));

        List<PessoaSalarioConsolidado> salariosConsolidados = new ArrayList<>();

        for (Pessoa pessoa : pessoas) {
            if (pessoa.getCargo() == null) {
                continue;
            }

            Cargo cargoDaPessoa = mapaDeCargos.get(pessoa.getCargo().getId());
            if (cargoDaPessoa == null) {
                continue;
            }

            BigDecimal salarioCalculado = BigDecimal.ZERO;

            for (CargoVencimento cargoVencimento : cargoDaPessoa.getVencimentos()) {
                Vencimento vencimento = cargoVencimento.getVencimento();
                if (vencimento.getTipo().equals(TipoVencimento.CREDITO)) {
                    salarioCalculado = salarioCalculado.add(vencimento.getValor());
                } else if (vencimento.getTipo().equals(TipoVencimento.DEBITO)) {
                    salarioCalculado = salarioCalculado.subtract(vencimento.getValor());
                }
            }

            // Cria o objeto de resultado
            PessoaSalarioConsolidado consolidado = new PessoaSalarioConsolidado();
            consolidado.setPessoaId(pessoa.getId());
            consolidado.setNome(pessoa.getNome());
            consolidado.setCargo(cargoDaPessoa.getNome());
            consolidado.setSalario(salarioCalculado);

            salariosConsolidados.add(consolidado);
        }

        // Usa o DAO para limpar a tabela antiga e inserir todos os novos registros
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
