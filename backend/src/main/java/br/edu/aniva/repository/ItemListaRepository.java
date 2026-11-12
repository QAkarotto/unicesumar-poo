package br.edu.aniva.repository;

import br.edu.aniva.domain.ItemLista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemListaRepository extends JpaRepository<ItemLista, Long> {

    List<ItemLista> findByUsuarioId(Long usuarioId);

    Optional<ItemLista> findByUsuarioIdAndObraId(Long usuarioId, String obraId);
}
