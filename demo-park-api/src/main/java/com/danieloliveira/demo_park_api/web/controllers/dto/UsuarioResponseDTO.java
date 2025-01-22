package com.danieloliveira.demo_park_api.web.controllers.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String role;
}
