package com.danieloliveira.demo_park_api.web.controllers;

import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
}
