package br.edu.lojajogos.service;

import br.edu.lojajogos.domain.Pedido;
import br.edu.lojajogos.dto.CriarPedidoRequest;
import br.edu.lojajogos.dto.PedidoResponse;
import br.edu.lojajogos.exception.RecursoNaoEncontradoException;
import br.edu.lojajogos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final JogoService jogoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteService clienteService,
                         JogoService jogoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.jogoService = jogoService;
    }

    /**
     * Caso de uso "comprar": busca cliente e jogo, deixa o domínio aplicar as regras e salva.
     * @Transactional garante que o INSERT do pedido e o UPDATE do estoque
     * acontecem juntos: se um falhar, nenhum é gravado.
     */
    @Transactional
    public PedidoResponse comprar(CriarPedidoRequest request) {
        if (request.clienteId() == null || request.jogoId() == null || request.quantidade() == null) {
            throw new IllegalArgumentException("Informe cliente, jogo e quantidade.");
        }

        var cliente = clienteService.buscarEntidade(request.clienteId());
        var jogo = jogoService.buscarEntidade(request.jogoId());

        var pedido = new Pedido(cliente, jogo, request.quantidade());

        return PedidoResponse.de(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        var pedido = buscarEntidade(id);
        pedido.cancelar();
        return PedidoResponse.de(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAllByOrderByDataPedidoDesc().stream()
                .map(PedidoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return PedidoResponse.de(buscarEntidade(id));
    }

    private Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido " + id + " não encontrado."));
    }
}