package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Optional<Turma> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
