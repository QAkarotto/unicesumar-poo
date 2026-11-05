package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, String> {
}
