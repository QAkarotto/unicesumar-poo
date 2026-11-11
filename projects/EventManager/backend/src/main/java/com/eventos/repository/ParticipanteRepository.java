package com.eventos.repository;

import com.eventos.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository
        extends JpaRepository<Participante, Long> {

    boolean existsByEmail(String email);
}
