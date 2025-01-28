package com.danieloliveira.demo_park_api.web.dto.mapper;


import com.danieloliveira.demo_park_api.entities.Cliente;
import com.danieloliveira.demo_park_api.web.dto.ClienteCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.ClienteResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

// classe estática que usa a biblioteca Model Mapper para converter um DTO para uma entitdade e vice-versa
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDTO dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDTO toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDTO.class);
    }
}
