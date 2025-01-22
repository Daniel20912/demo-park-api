package com.danieloliveira.demo_park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDTO {

    @NotBlank // Verifica se não é nulo e se tem pelo menos um caracter
    // o regexp indica que antes e depois do @ deve ter uma quantidade ilimitade de caracteres e a obrigatoriedade de um ponto e permitindo apenas letras maiúsculas e minúsculas.
    //{2,}: Exige pelo menos 2 letras para a TLD, sem limite superior (exemplo: .com, .info,
    @Email(message = "Formato do email inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}

