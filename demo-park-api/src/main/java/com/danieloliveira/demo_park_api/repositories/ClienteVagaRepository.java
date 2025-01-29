package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
}