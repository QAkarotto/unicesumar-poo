package br.edu.seugame.repository;

import br.edu.seugame.domain.Participante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findByTorneioIdOrderByOrdemSorteioAscIdAsc(Long torneioId);
}
