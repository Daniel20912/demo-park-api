package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}