package br.edu.seugame.repository;

import br.edu.seugame.domain.Classificacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {
    @EntityGraph(attributePaths="participante")
    List<Classificacao> findByTorneioId(Long torneioId);
    Optional<Classificacao> findByTorneioIdAndParticipanteId(Long torneioId, Long participanteId);
}
