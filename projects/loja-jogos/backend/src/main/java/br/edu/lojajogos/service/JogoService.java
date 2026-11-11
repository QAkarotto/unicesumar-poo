package br.edu.lojajogos.service;

import br.edu.lojajogos.domain.Jogo;
import br.edu.lojajogos.dto.JogoRequest;
import br.edu.lojajogos.dto.JogoResponse;
import br.edu.lojajogos.exception.RecursoNaoEncontradoException;
import br.edu.lojajogos.repository.JogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    @Transactional
    public JogoResponse cadastrar(JogoRequest request) {
        var jogo = new Jogo(request.titulo(), request.plataforma(), request.preco(), request.estoque());
        return JogoResponse.de(jogoRepository.save(jogo));
    }

    @Transactional(readOnly = true)
    public List<JogoResponse> listar() {
        return jogoRepository.findAll().stream()
                .map(JogoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public JogoResponse buscarPorId(Long id) {
        return JogoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public JogoResponse atualizar(Long id, JogoRequest request) {
        var jogo = buscarEntidade(id);
        jogo.atualizar(request.titulo(), request.plataforma(), request.preco(), request.estoque());
        // Sem save(): o objeto veio do banco dentro da transação, então o JPA
        // percebe a mudança e faz o UPDATE sozinho no fim do método (dirty checking).
        return JogoResponse.de(jogo);
    }

    public Jogo buscarEntidade(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogo " + id + " não encontrado."));
    }
}