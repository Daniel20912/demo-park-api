package com.danieloliveira.demo_park_api.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
}
