package br.edu.aniva.repository;

import br.edu.aniva.domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByObraIdOrderByCriadoEmAsc(String obraId);
}
