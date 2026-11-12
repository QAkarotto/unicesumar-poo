package br.edu.controleestoque.repository;

import br.edu.controleestoque.domain.Movimentacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    boolean existsByProdutoId(Long produtoId);

    List<Movimentacao> findAllByOrderByDataHoraDescIdDesc();
}
