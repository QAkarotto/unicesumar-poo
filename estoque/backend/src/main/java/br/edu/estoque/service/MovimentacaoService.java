package br.edu.estoque.service;

import br.edu.estoque.domain.*;
import br.edu.estoque.dto.MovimentacaoRequest;
import br.edu.estoque.exception.RecursoNaoEncontradoException;
import br.edu.estoque.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoService {
    private final ProdutoRepository produtos;
    private final MovimentacaoRepository movimentacoes;

    public MovimentacaoService(ProdutoRepository produtos, MovimentacaoRepository movimentacoes) {
        this.produtos = produtos; this.movimentacoes = movimentacoes;
    }

    @Transactional
    public Movimentacao registrar(MovimentacaoRequest r) {
        var produto = produtos.findById(r.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
        var mov = r.tipo() == TipoMovimentacao.ENTRADA
                ? produto.registrarEntrada(r.quantidade())
                : produto.registrarSaida(r.quantidade());
        return movimentacoes.save(mov);
    }

    public List<Movimentacao> listar(Long produtoId) {
        return produtoId == null ? movimentacoes.findAllByOrderByDataHoraDesc()
                : movimentacoes.findByProdutoIdOrderByDataHoraDesc(produtoId);
    }
}
