package br.edu.estoque.repository;

import br.edu.estoque.domain.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByCategoriaId(Long categoriaId);
    boolean existsBySku(String sku);

    @Query("select p from Produto p where p.ativo = true and p.quantidade < p.estoqueMinimo")
    List<Produto> findAbaixoDoMinimo();
}
