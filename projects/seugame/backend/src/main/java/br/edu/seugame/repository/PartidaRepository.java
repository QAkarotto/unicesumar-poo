package br.edu.seugame.repository;

import br.edu.seugame.domain.Partida;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @EntityGraph(attributePaths={"participante1","participante2","vencedor"})
    List<Partida> findByTorneioIdOrderByRodadaAscOrdemAsc(Long torneioId);
    Optional<Partida> findByIdAndTorneioId(Long id, Long torneioId);
}
