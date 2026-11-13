package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.Fornecedor;
import br.edu.controleestoque.domain.Movimentacao;
import br.edu.controleestoque.domain.TipoMovimentacao;
import br.edu.controleestoque.api.MovimentacaoRequest;
import br.edu.controleestoque.api.MovimentacaoResponse;
import br.edu.controleestoque.domain.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Coordena o caso de uso "movimentar estoque": carrega os objetos, delega a
 * regra ao domínio (Produto) e persiste o resultado. Tudo numa transação:
 * se a regra falhar, nem a movimentação nem o novo saldo são gravados.
 */
@Service
public class MovimentacaoService {

    private final MovimentacaoRepository repository;
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;

    public MovimentacaoService(MovimentacaoRepository repository, ProdutoService produtoService,
                               FornecedorService fornecedorService) {
        this.repository = repository;
        this.produtoService = produtoService;
        this.fornecedorService = fornecedorService;
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest req) {
        // Lock pessimista: movimentações simultâneas do mesmo produto são processadas uma de cada vez
        var produto = produtoService.obterParaMovimentacao(req.produtoId());

        Movimentacao movimentacao = switch (req.tipo()) {
            case ENTRADA -> {
                Fornecedor fornecedor = req.fornecedorId() == null
                        ? null : fornecedorService.obter(req.fornecedorId());
                yield produto.registrarEntrada(req.quantidade(), fornecedor, req.observacao());
            }
            case SAIDA -> produto.registrarSaida(req.quantidade(), req.observacao());
            case AJUSTE -> produto.ajustarEstoque(req.novoSaldo(), req.observacao());
        };

        // INSERT da movimentação; o UPDATE do saldo do produto é feito por dirty checking no commit
        return MovimentacaoResponse.de(repository.save(movimentacao));
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listar(Long produtoId, TipoMovimentacao tipo) {
        return repository.pesquisar(produtoId, tipo).stream().map(MovimentacaoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoResponse buscar(Long id) {
        return repository.findById(id)
                .map(MovimentacaoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação", id));
    }
}
