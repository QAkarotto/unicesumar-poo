package br.edu.aniva.repository;

import br.edu.aniva.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByObraId(String obraId);

    Optional<Avaliacao> findByUsuarioIdAndObraId(Long usuarioId, String obraId);
}
