package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<Categoria> findAllByOrderByNomeAsc();
}
