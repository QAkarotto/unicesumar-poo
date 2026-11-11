package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.OfertaDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfertaDisciplinaRepository extends JpaRepository<OfertaDisciplina, Long> {

    Optional<OfertaDisciplina> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<OfertaDisciplina> findByTurmaCodigo(String turmaCodigo);
}
