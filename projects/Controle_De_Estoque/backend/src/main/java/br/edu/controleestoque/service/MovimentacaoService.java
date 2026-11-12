package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.AcaoLog;
import br.edu.controleestoque.domain.EntidadeLog;
import br.edu.controleestoque.domain.Movimentacao;
import br.edu.controleestoque.domain.TipoMovimentacao;
import br.edu.controleestoque.dto.MovimentacaoRequest;
import br.edu.controleestoque.dto.MovimentacaoResponse;
import br.edu.controleestoque.exception.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso de movimentação de estoque.
 * O serviço apenas coordena: carrega o produto, pede a ele que registre a
 * entrada ou saída (onde ficam as regras de saldo), grava a movimentação e o log.
 * Tudo ocorre em uma única transação: saldo, movimentação e log são salvos juntos.
 * Movimentações são histórico, por isso não podem ser editadas nem excluídas.
 */
@Service
public class MovimentacaoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final LogService logService;

    public MovimentacaoService(ProdutoRepository produtoRepository,
                               MovimentacaoRepository movimentacaoRepository,
                               LogService logService) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.logService = logService;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listar() {
        return movimentacaoRepository.findAllByOrderByDataHoraDescIdDesc().stream()
                .map(MovimentacaoResponse::de)
                .toList();
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest request) {
        var produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));

        Movimentacao movimentacao;
        AcaoLog acao;
        if (request.tipo() == TipoMovimentacao.ENTRADA) {
            movimentacao = produto.registrarEntrada(request.quantidade(), request.motivo());
            acao = AcaoLog.ENTRADA;
        } else {
            movimentacao = produto.registrarSaida(request.quantidade(), request.motivo());
            acao = AcaoLog.SAIDA;
        }

        movimentacaoRepository.save(movimentacao);
        logService.registrar(acao, EntidadeLog.MOVIMENTACAO, movimentacao.getId(),
                request.tipo() + " de " + request.quantidade() + " un. do produto "
                        + produto.getCodigo() + " - " + produto.getNome()
                        + " (saldo: " + produto.getQuantidade() + ")");
        return MovimentacaoResponse.de(movimentacao);
    }
}
