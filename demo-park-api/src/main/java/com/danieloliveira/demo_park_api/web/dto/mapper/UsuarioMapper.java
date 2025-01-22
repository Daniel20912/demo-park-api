package com.danieloliveira.demo_park_api.web.dto.mapper;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.web.dto.UsuarioCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

// classe estáticaq que usa a biblioteca Model Mapper para converter um DTO para uma entitdade e vice-versa
public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDTO createDTO) {
        return new ModelMapper().map(createDTO, Usuario.class);
    }

    public static UsuarioResponseDTO toDto(Usuario usuario) {
        // remove o ROLE_ do nome do enum (se for ROLE_ADMIN, vai ficar só ADMIN)
        String role = usuario.getRole().name().substring("ROLE_".length());

        // insere a string role no role da UsuarioResponseDTO
        // usuario é a classe fonte e UsuarioResponseDTO a classe de destino
        PropertyMap<Usuario, UsuarioResponseDTO> props = new PropertyMap<>() {
            protected void configure() {
                // a partir do map temos acesso aos campos do UsuarioResponseDTO
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    public static List<UsuarioResponseDTO> toListDto(List<Usuario> usuarios) {
        // transforma cada usuário em da lista em um UsuarioResponseDTO
        return usuarios.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }
}
