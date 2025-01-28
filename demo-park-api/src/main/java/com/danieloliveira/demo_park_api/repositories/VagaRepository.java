package com.danieloliveira.demo_park_api.repositories;

import com.danieloliveira.demo_park_api.entities.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
}
