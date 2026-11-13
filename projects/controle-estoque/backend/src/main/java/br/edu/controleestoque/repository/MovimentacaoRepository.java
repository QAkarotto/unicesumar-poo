package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Movimentacao;
import br.edu.controleestoque.domain.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query("""
            SELECT m FROM Movimentacao m
              JOIN FETCH m.produto p
              LEFT JOIN FETCH m.fornecedor
            WHERE (:produtoId IS NULL OR p.id = :produtoId)
              AND (:tipo IS NULL OR m.tipo = :tipo)
            ORDER BY m.dataHora DESC, m.id DESC
            """)
    List<Movimentacao> pesquisar(@Param("produtoId") Long produtoId,
                                 @Param("tipo") TipoMovimentacao tipo);

    boolean existsByFornecedorId(Long fornecedorId);
}
