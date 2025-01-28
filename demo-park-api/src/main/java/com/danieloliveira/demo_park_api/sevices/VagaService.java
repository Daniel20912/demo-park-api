package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Vaga;
import com.danieloliveira.demo_park_api.exceptions.CodigoUniqueViolationException;
import com.danieloliveira.demo_park_api.exceptions.EntityNotFoundException;
import com.danieloliveira.demo_park_api.repositories.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException e) {
            throw new CodigoUniqueViolationException(String.format("Vaga com codigo %s já cadastrada", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga com código '%s' não foi encotrada", codigo))
        );
    }
}
