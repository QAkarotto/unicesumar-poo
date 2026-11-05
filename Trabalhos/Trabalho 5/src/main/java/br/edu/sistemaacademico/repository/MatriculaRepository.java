package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}
