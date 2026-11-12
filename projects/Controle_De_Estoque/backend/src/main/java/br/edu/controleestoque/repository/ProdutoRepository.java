package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCategoriaId(Long categoriaId);
}
