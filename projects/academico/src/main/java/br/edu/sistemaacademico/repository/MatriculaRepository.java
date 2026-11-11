package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Matricula> findByAlunoRegistroAcademico(String registroAcademico);

    List<Matricula> findByOfertaDisciplinaCodigo(String ofertaCodigo);

    boolean existsByAlunoRegistroAcademicoAndOfertaDisciplinaCodigo(String ra, String ofertaCodigo);

    boolean existsByAlunoRegistroAcademicoAndOfertaDisciplinaId(String ra, Long ofertaId);
}
