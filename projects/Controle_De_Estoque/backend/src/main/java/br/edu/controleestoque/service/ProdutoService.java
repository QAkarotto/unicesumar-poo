package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.AcaoLog;
import br.edu.controleestoque.domain.Categoria;
import br.edu.controleestoque.domain.EntidadeLog;
import br.edu.controleestoque.domain.Produto;
import br.edu.controleestoque.dto.ProdutoRequest;
import br.edu.controleestoque.dto.ProdutoResponse;
import br.edu.controleestoque.exception.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.CategoriaRepository;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final LogService logService;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository,
                          MovimentacaoRepository movimentacaoRepository,
                          LogService logService) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.logService = logService;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return ProdutoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        if (produtoRepository.existsByCodigoIgnoreCase(request.codigo().trim())) {
            throw new IllegalStateException("Já existe um produto com o código informado.");
        }

        var categoria = buscarCategoria(request.categoriaId());
        var produto = produtoRepository.save(
                new Produto(request.codigo(), request.nome(), categoria, request.estoqueMinimo()));
        logService.registrar(AcaoLog.CRIAR, EntidadeLog.PRODUTO, produto.getId(),
                "Produto criado: " + descrever(produto));
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        var produto = buscarEntidade(id);
        var categoria = buscarCategoria(request.categoriaId());

        produto.atualizar(request.nome(), categoria, request.estoqueMinimo());
        logService.registrar(AcaoLog.ATUALIZAR, EntidadeLog.PRODUTO, id,
                "Produto atualizado: " + descrever(produto));
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse inativar(Long id) {
        var produto = buscarEntidade(id);

        produto.inativar();
        logService.registrar(AcaoLog.INATIVAR, EntidadeLog.PRODUTO, id,
                "Produto inativado: " + descrever(produto));
        return ProdutoResponse.de(produto);
    }

    @Transactional
    public ProdutoResponse ativar(Long id) {
        var produto = buscarEntidade(id);

        produto.ativar();
        logService.registrar(AcaoLog.ATIVAR, EntidadeLog.PRODUTO, id,
                "Produto ativado: " + descrever(produto));
        return ProdutoResponse.de(produto);
    }

    /**
     * Produto com histórico de movimentações não pode ser excluído,
     * pois o histórico deve ser preservado; nesse caso ele deve ser inativado.
     */
    @Transactional
    public void excluir(Long id) {
        var produto = buscarEntidade(id);
        if (movimentacaoRepository.existsByProdutoId(id)) {
            throw new IllegalStateException(
                    "Produto com movimentações não pode ser excluído. Inative o produto.");
        }

        produtoRepository.delete(produto);
        logService.registrar(AcaoLog.EXCLUIR, EntidadeLog.PRODUTO, id,
                "Produto excluído: " + descrever(produto));
    }

    private Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada."));
    }

    private String descrever(Produto produto) {
        return produto.getCodigo() + " - " + produto.getNome();
    }
}
