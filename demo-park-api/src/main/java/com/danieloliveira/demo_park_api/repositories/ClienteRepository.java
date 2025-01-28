package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.Cliente;
import com.danieloliveira.demo_park_api.repositories.projection.ClienteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClienteProjection> findAllPageble(Pageable page);

    Cliente findByUsuarioId(Long id);
}
