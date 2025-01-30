package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.ClienteVaga;
import com.danieloliveira.demo_park_api.exceptions.EntityNotFoundException;
import com.danieloliveira.demo_park_api.repositories.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
        // se a data de saída é nula signifaca que o o cliente ainda está estacionado
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(() -> new EntityNotFoundException("Recibo " + recibo + " não encontrado no sistema ou check-out já realizado"));
    }
}
