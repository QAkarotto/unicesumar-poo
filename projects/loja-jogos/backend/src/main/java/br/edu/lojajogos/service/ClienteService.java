package br.edu.lojajogos.service;

import br.edu.lojajogos.domain.Cliente;
import br.edu.lojajogos.dto.ClienteResponse;
import br.edu.lojajogos.dto.CriarClienteRequest;
import br.edu.lojajogos.exception.RecursoNaoEncontradoException;
import br.edu.lojajogos.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponse cadastrar(CriarClienteRequest request) {
        var cliente = new Cliente(request.nome(), request.email());

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalStateException("Já existe um cliente com este e-mail.");
        }

        return ClienteResponse.de(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return ClienteResponse.de(buscarEntidade(id));
    }

    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado."));
    }
}