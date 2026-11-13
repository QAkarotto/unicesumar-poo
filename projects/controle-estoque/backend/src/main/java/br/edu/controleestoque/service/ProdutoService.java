package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.Produto;
import br.edu.controleestoque.api.ProdutoDetalheResponse;
import br.edu.controleestoque.api.ProdutoRequest;
import br.edu.controleestoque.api.ProdutoResponse;
import br.edu.controleestoque.api.ResumoEstoqueResponse;
import br.edu.controleestoque.domain.OperacaoNaoPermitidaException;
import br.edu.controleestoque.domain.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;

    public ProdutoService(ProdutoRepository repository, CategoriaService categoriaService,
                          FornecedorService fornecedorService) {
        this.repository = repository;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(Long categoriaId, String busca) {
        String termo = (busca == null || busca.isBlank()) ? null : busca.trim();
        return repository.pesquisar(categoriaId, termo).stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDetalheResponse buscar(Long id) {
        var produto = repository.findDetalhadoById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
        return ProdutoDetalheResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest req) {
        if (repository.existsBySkuIgnoreCase(req.sku().trim())) {
            throw new OperacaoNaoPermitidaException("Já existe um produto com o SKU " + req.sku() + ".");
        }
        var categoria = categoriaService.obter(req.categoriaId());
        var produto = new Produto(req.sku(), req.nome(), req.descricao(), req.precoUnitario(),
                req.estoqueMinimo(), categoria);
        return ProdutoResponse.de(repository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest req) {
        var produto = obter(id);
        var categoria = categoriaService.obter(req.categoriaId());
        produto.atualizarDados(req.nome(), req.descricao(), req.precoUnitario(), req.estoqueMinimo(), categoria);
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse inativar(Long id) {
        var produto = obter(id);
        produto.inativar();
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse ativar(Long id) {
        var produto = obter(id);
        produto.ativar();
        return ProdutoResponse.de(produto);
    }

    /** Regra: só é possível excluir produto sem histórico de movimentações. */
    @Transactional
    public void excluir(Long id) {
        var produto = obter(id);
        if (produto.possuiMovimentacoes()) {
            throw new OperacaoNaoPermitidaException(
                    "O produto possui movimentações e não pode ser excluído. Inative-o.");
        }
        repository.delete(produto);
    }

    @Transactional
    public ProdutoDetalheResponse vincularFornecedor(Long produtoId, Long fornecedorId) {
        var produto = obterDetalhado(produtoId);
        produto.vincularFornecedor(fornecedorService.obter(fornecedorId));
        return ProdutoDetalheResponse.de(produto);
    }

    @Transactional
    public ProdutoDetalheResponse desvincularFornecedor(Long produtoId, Long fornecedorId) {
        var produto = obterDetalhado(produtoId);
        produto.desvincularFornecedor(fornecedorService.obter(fornecedorId));
        return ProdutoDetalheResponse.de(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> paraReposicao() {
        return repository.findParaReposicao().stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ResumoEstoqueResponse resumo() {
        var produtos = repository.findAll();
        long ativos = produtos.stream().filter(Produto::isAtivo).count();
        long reposicao = produtos.stream().filter(Produto::precisaReposicao).count();
        long unidades = produtos.stream().mapToLong(Produto::getQuantidadeEmEstoque).sum();
        BigDecimal valor = produtos.stream().map(Produto::valorEmEstoque)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ResumoEstoqueResponse(produtos.size(), ativos, reposicao, unidades, valor);
    }

    Produto obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    /** Busca com lock pessimista: só deve ser chamado dentro de uma transação de movimentação. */
    Produto obterParaMovimentacao(Long id) {
        return repository.findParaMovimentacaoById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    private Produto obterDetalhado(Long id) {
        return repository.findDetalhadoById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }
}
