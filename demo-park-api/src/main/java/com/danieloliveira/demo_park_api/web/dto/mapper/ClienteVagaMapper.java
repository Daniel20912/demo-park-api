package com.danieloliveira.demo_park_api.web.dto.mapper;

import com.danieloliveira.demo_park_api.entities.ClienteVaga;
import com.danieloliveira.demo_park_api.web.dto.EstacionamentoCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.EstacionamentoResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDTO dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDTO toDto(ClienteVaga vaga) {
        return new ModelMapper().map(vaga, EstacionamentoResponseDTO.class);
    }


}
