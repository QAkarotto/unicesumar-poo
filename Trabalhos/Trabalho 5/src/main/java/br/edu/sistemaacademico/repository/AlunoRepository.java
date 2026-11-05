package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, String> {

    boolean existsByEmail(String email);
}
