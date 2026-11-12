package br.edu.aniva.repository;

import br.edu.aniva.domain.Obra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObraRepository extends JpaRepository<Obra, String> {
}
