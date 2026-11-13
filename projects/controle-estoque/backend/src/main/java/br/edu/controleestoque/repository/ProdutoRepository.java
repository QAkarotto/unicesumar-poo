package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Produto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    /** Busca por nome/SKU e categoria (filtros opcionais). JOIN FETCH evita o problema N+1. */
    @Query("""
            SELECT p FROM Produto p JOIN FETCH p.categoria c
            WHERE (:categoriaId IS NULL OR c.id = :categoriaId)
              AND (:busca IS NULL
                   OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
                   OR LOWER(p.sku)  LIKE LOWER(CONCAT('%', :busca, '%')))
            ORDER BY p.nome
            """)
    List<Produto> pesquisar(@Param("categoriaId") Long categoriaId, @Param("busca") String busca);

    /** Produtos ativos cujo saldo atingiu ou ficou abaixo do estoque mínimo. */
    @Query("""
            SELECT p FROM Produto p JOIN FETCH p.categoria
            WHERE p.ativo = true AND p.quantidadeEmEstoque <= p.estoqueMinimo
            ORDER BY p.quantidadeEmEstoque
            """)
    List<Produto> findParaReposicao();

    /**
     * Carrega o produto travando a linha (SELECT ... FOR UPDATE) até o fim da transação.
     * Usado nas movimentações: uma segunda movimentação do mesmo produto espera a primeira
     * terminar e lê o saldo já atualizado, evitando a perda de atualização (lost update).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Produto p WHERE p.id = :id")
    Optional<Produto> findParaMovimentacaoById(@Param("id") Long id);

    /** Carrega categoria e fornecedores numa única consulta. */
    @EntityGraph(attributePaths = {"categoria", "fornecedores"})
    @Query("SELECT p FROM Produto p WHERE p.id = :id")
    Optional<Produto> findDetalhadoById(@Param("id") Long id);
}
