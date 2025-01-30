package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Cliente;
import com.danieloliveira.demo_park_api.entities.ClienteVaga;
import com.danieloliveira.demo_park_api.entities.Vaga;
import com.danieloliveira.demo_park_api.utils.EstacionamentoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.danieloliveira.demo_park_api.entities.Vaga.StatusVaga.LIVRE;
import static com.danieloliveira.demo_park_api.entities.Vaga.StatusVaga.OCUPADA;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {

        // quando for salvar no Banco de Dados precisa ter o objeto Cliente, pois ele faz parte do mapeamento do ClienteVaga
        // assim o relacionamento entre a tabela Clientes e a tabela Clientes_Tem_Vaga, vai se realizado
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());

        clienteVaga.setCliente(cliente); // assim substitui o objeto cliente que tinha anteiromenente só com o cpf, por um objeto cliente completo

        Vaga vaga = vagaService.buscarPorVagaLivre();

        vaga.setStatus(OCUPADA); // transforma a vaga que estava livre em ocupada

        // dessa forma quando o ClienteVaga for salvo no banco de dados, vai ser feito o relacionamento da vaga com Clientes_Tem_Vagas
        // e a vaga que está como livre na vaga de Vagas, recebe um update e passa a estar ocupada
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamentoUtil.gerarRecibo());
        return clienteVagaService.salvar(clienteVaga);
    }

    @Transactional
    public ClienteVaga checkout(String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);

        LocalDateTime dataSaida = LocalDateTime.now();
        BigDecimal valor = EstacionamentoUtil.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
        clienteVaga.setValor(valor);

        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

        BigDecimal desconto = EstacionamentoUtil.calcularDesconto(valor, totalDeVezes);

        clienteVaga.setDesconto(desconto);

        clienteVaga.setDataSaida(dataSaida);

        clienteVaga.getVaga().setStatus(LIVRE);

        clienteVagaService.salvar(clienteVaga);

        return clienteVagaService.salvar(clienteVaga);
    }
}
