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
import teste.augusto.model.Cargo;
import teste.augusto.model.CargoVencimento;
import teste.augusto.model.Pessoa;
import teste.augusto.model.Vencimento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
        // 1. Criar Vencimentos de Exemplo
        Vencimento salarioBase = new Vencimento(); // ID, Descricao, Valor, Tipo
        salarioBase.setValor(new BigDecimal("2500.00"));
        salarioBase.setTipo(TipoVencimento.CREDITO);

        Vencimento planoSaude = new Vencimento();
        planoSaude.setValor(new BigDecimal("350.00"));
        planoSaude.setTipo(TipoVencimento.DEBITO);

        Vencimento previdencia = new Vencimento();
        previdencia.setValor(new BigDecimal("11.00"));
        previdencia.setTipo(TipoVencimento.DEBITO);

        // 2. Criar Cargo e associar Vencimentos
        cargoAnalista = new Cargo();
        cargoAnalista.setId(1);
        cargoAnalista.setNome("Analista");

        List<CargoVencimento> vencimentosDoCargo = new ArrayList<>();
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, salarioBase));
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, planoSaude));
        vencimentosDoCargo.add(new CargoVencimento(cargoAnalista, previdencia));
        cargoAnalista.setVencimentos(vencimentosDoCargo); // Supondo que você tem um setter para isso

        // 3. Criar Pessoa e associar Cargo
        pessoaAnalista = new Pessoa();
        pessoaAnalista.setId(100);
        pessoaAnalista.setNome("Fulano de Tal");
        pessoaAnalista.setCargo(cargoAnalista);
    }

    @Test
    @DisplayName("Deve calcular o salário de um Analista corretamente")
    void deveCalcularSalarioDeAnalistaCorretamente() {
        // ARRANGE (Arranjar / Configurar)
        // Dizemos ao Mockito: "Quando o método findAll do pessoaDAO for chamado,
        // retorne uma lista contendo a nossa pessoa de exemplo."
        when(pessoaDAO.findAll()).thenReturn(Collections.singletonList(pessoaAnalista));

        // "Quando o método buscarTodosComVencimentos do cargoDAO for chamado,
        // retorne uma lista contendo o nosso cargo de exemplo."
        when(cargoDAO.buscarTodosComVencimentos()).thenReturn(Collections.singletonList(cargoAnalista));

        // ACT (Agir)
        // Chamamos o método que queremos testar.
        salarioService.calcularEPreencherSalarios();

        // ASSERT (Verificar)
        // Verificamos se o resultado foi o esperado.
        // O salário esperado é 2500 (CREDITO) - 350 (DEBITO) - 11 (DEBITO) = 2139.00
        BigDecimal salarioEsperado = new BigDecimal("2139.00");

        // O Mockito nos permite "capturar" o argumento que foi passado para um método de um mock.
        // Aqui, queremos verificar se o consolidadoDAO.limparEInserirTodos() foi chamado
        // com uma lista contendo um objeto com o salário correto.
        verify(consolidadoDAO, times(1)).limparEInserirTodos(argThat(lista -> {
            // Verifica se a lista não está vazia e se o salário do primeiro elemento é o esperado
            return !lista.isEmpty() &&
                    lista.get(0).getPessoaId().equals(pessoaAnalista.getId()) &&
                    lista.get(0).getSalario().compareTo(salarioEsperado) == 0;
        }));
    }
}
