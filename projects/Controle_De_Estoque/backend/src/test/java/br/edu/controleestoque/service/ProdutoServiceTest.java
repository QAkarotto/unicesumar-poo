package br.edu.controleestoque.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.controleestoque.domain.AcaoLog;
import br.edu.controleestoque.domain.Categoria;
import br.edu.controleestoque.domain.EntidadeLog;
import br.edu.controleestoque.domain.Produto;
import br.edu.controleestoque.dto.ProdutoRequest;
import br.edu.controleestoque.repository.CategoriaRepository;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProdutoServiceTest {

    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;
    private MovimentacaoRepository movimentacaoRepository;
    private LogService logService;
    private ProdutoService service;

    @BeforeEach
    void preparar() {
        produtoRepository = mock(ProdutoRepository.class);
        categoriaRepository = mock(CategoriaRepository.class);
        movimentacaoRepository = mock(MovimentacaoRepository.class);
        logService = mock(LogService.class);
        service = new ProdutoService(produtoRepository, categoriaRepository, movimentacaoRepository, logService);
    }

    @Test
    void naoDevePermitirCodigoDuplicado() {
        when(produtoRepository.existsByCodigoIgnoreCase("P001")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.criar(new ProdutoRequest("P001", "Teclado", 1L, 5)));

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveRegistrarLogAoCriarProduto() {
        var categoria = new Categoria("Periféricos");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(chamada -> chamada.getArgument(0));

        service.criar(new ProdutoRequest("P001", "Teclado", 1L, 5));

        verify(logService).registrar(any(AcaoLog.class), any(EntidadeLog.class), any(), any());
    }

    @Test
    void naoDevePermitirExcluirProdutoComMovimentacoes() {
        var produto = new Produto("P001", "Teclado", new Categoria("Periféricos"), 5);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentacaoRepository.existsByProdutoId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.excluir(1L));

        verify(produtoRepository, never()).delete(any());
        verify(logService, never()).registrar(any(), any(), any(), any());
    }
}
