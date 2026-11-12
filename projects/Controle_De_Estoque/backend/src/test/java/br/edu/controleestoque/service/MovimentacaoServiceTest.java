package br.edu.controleestoque.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import br.edu.controleestoque.domain.TipoMovimentacao;
import br.edu.controleestoque.dto.MovimentacaoRequest;
import br.edu.controleestoque.exception.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovimentacaoServiceTest {

    private ProdutoRepository produtoRepository;
    private MovimentacaoRepository movimentacaoRepository;
    private LogService logService;
    private MovimentacaoService service;
    private Produto produto;

    @BeforeEach
    void preparar() {
        produtoRepository = mock(ProdutoRepository.class);
        movimentacaoRepository = mock(MovimentacaoRepository.class);
        logService = mock(LogService.class);
        service = new MovimentacaoService(produtoRepository, movimentacaoRepository, logService);

        produto = new Produto("P001", "Teclado", new Categoria("Periféricos"), 2);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
    }

    @Test
    void deveRegistrarEntradaAtualizandoSaldoELog() {
        var resposta = service.registrar(new MovimentacaoRequest(1L, TipoMovimentacao.ENTRADA, 10, "Compra"));

        assertThat(produto.getQuantidade()).isEqualTo(10);
        assertThat(resposta.tipo()).isEqualTo("ENTRADA");
        verify(movimentacaoRepository).save(any());
        verify(logService).registrar(any(AcaoLog.class), any(EntidadeLog.class), any(), any());
    }

    @Test
    void naoDeveRegistrarNemLogarSaidaSemSaldo() {
        var pedido = new MovimentacaoRequest(1L, TipoMovimentacao.SAIDA, 1, "Venda");

        assertThrows(IllegalStateException.class, () -> service.registrar(pedido));

        verify(movimentacaoRepository, never()).save(any());
        verify(logService, never()).registrar(any(), any(), any(), any());
    }

    @Test
    void naoDeveMovimentarProdutoInexistente() {
        var pedido = new MovimentacaoRequest(99L, TipoMovimentacao.ENTRADA, 1, null);
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.registrar(pedido));
    }
}
