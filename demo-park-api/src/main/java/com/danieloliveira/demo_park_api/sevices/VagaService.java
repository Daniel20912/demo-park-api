package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Vaga;
import com.danieloliveira.demo_park_api.exceptions.CodigoUniqueViolationException;
import com.danieloliveira.demo_park_api.exceptions.EntityNotFoundException;
import com.danieloliveira.demo_park_api.exceptions.VagaDisponivelException;
import com.danieloliveira.demo_park_api.repositories.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.danieloliveira.demo_park_api.entities.Vaga.StatusVaga.LIVRE;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException e) {
            throw new CodigoUniqueViolationException("Vaga", vaga.getCodigo());
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException("Vaga", codigo)
        );
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorVagaLivre() {
        return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(() -> new VagaDisponivelException("Nenhuma vaga livre for encontrada"));
    }
}
