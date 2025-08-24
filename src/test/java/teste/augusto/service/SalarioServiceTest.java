package teste.augusto.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teste.augusto.dao.CargoDAO;
import teste.augusto.dao.PessoaDAO;
import teste.augusto.dao.PessoaSalarioConsolidadoDAO;
import teste.augusto.enums.TipoVencimento;
import teste.augusto.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalarioServiceTest {

    @Mock
    private PessoaDAO pessoaDAO;

    @Mock
    private CargoDAO cargoDAO;

    @Mock
    private PessoaSalarioConsolidadoDAO consolidadoDAO;

    @InjectMocks
    private SalarioService salarioService;

    private Pessoa pessoaAnalista;
    private Cargo cargoAnalista;


    @BeforeEach
    void setUp() {

        Vencimento salarioBase = new Vencimento();
        salarioBase.setValor(new BigDecimal("2500.00"));
        salarioBase.setTipo(TipoVencimento.CREDITO);

        Vencimento planoSaude = new Vencimento();
        planoSaude.setValor(new BigDecimal("350.00"));
        planoSaude.setTipo(TipoVencimento.DEBITO);

        Vencimento previdencia = new Vencimento();
        previdencia.setValor(new BigDecimal("11.00"));
        previdencia.setTipo(TipoVencimento.DEBITO);

        cargoAnalista = new Cargo();
        cargoAnalista.setId(3);
        cargoAnalista.setNome("Analista");

        List<CargoVencimento> vencimentosDoCargo = new ArrayList<>();
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, salarioBase));
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, planoSaude));
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, previdencia));
        cargoAnalista.setVencimentos(vencimentosDoCargo);

        pessoaAnalista = new Pessoa();
        pessoaAnalista.setId(101);
        pessoaAnalista.setNome("Pessoa de Teste");
        pessoaAnalista.setEmail("teste@teste.com");
        pessoaAnalista.setCargo(cargoAnalista);
    }

    @Test
    @DisplayName("Deve calcular o salário de um Analista corretamente")
    void deveCalcularSalarioDeAnalistaCorretamente() {

        when(pessoaDAO.findAll()).thenReturn(Collections.singletonList(pessoaAnalista));
        when(cargoDAO.buscarTodosComVencimentos()).thenReturn(Collections.singletonList(cargoAnalista));

        salarioService.calcularEPreencherSalarios();

        BigDecimal salarioEsperado = new BigDecimal("2139.00");

        verify(consolidadoDAO, times(1)).limparEInserirTodos(argThat(lista -> {
            PessoaSalarioConsolidado resultado = lista.get(0);

            return !lista.isEmpty() &&
                    resultado.getPessoaId().equals(pessoaAnalista.getId()) &&
                    resultado.getNome().equals(pessoaAnalista.getNome()) &&
                    resultado.getCargo().equals(cargoAnalista.getNome()) &&
                    resultado.getSalario().compareTo(salarioEsperado) == 0;
        }));
    }
}