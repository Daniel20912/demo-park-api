package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // é preciso usar uma Query ao inves de palavras-chave porque o jpa interpreta Enums como um array
    @Query("SELECT u.role from Usuario u where u.username like :username")
    Usuario.Role findRoleByUsername(String username);
}