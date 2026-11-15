package br.edu.estoque.repository;

import br.edu.estoque.domain.Movimentacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByProdutoIdOrderByDataHoraDesc(Long produtoId);
    List<Movimentacao> findAllByOrderByDataHoraDesc();
}
