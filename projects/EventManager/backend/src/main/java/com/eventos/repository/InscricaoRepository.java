
package com.eventos.repository;

import com.eventos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscricaoRepository
        extends JpaRepository<Inscricao, Long> {

    boolean existsByEventoIdAndParticipanteId(
        Long eventoId, Long participanteId
    );

    long countByEventoId(Long eventoId);

    List<Inscricao> findByEventoId(Long eventoId);

    List<Inscricao> findByParticipanteId(Long participanteId);
}
