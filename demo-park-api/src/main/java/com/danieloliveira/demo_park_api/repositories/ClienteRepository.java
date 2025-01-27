package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
