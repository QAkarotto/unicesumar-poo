package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, String> {

    Optional<Aluno> findByEmail(String email);

    Optional<Aluno> findByRegistroAcademico(String registroAcademico);

    boolean existsByRegistroAcademico(String registroAcademico);
}
