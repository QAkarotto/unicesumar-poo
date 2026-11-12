package br.com.vetagenda.service;

import br.com.vetagenda.domain.Especialidade;
import br.com.vetagenda.dto.requests.EspecialidadeRequest;
import br.com.vetagenda.dto.responses.EspecialidadeResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.EspecialidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EspecialidadeService {
    private final EspecialidadeRepository repository;
    public EspecialidadeService(EspecialidadeRepository repository) { this.repository = repository; }
    @Transactional
    public EspecialidadeResponse cadastrar(EspecialidadeRequest req) {
        if (repository.existsByNomeIgnoreCase(req.nome())) throw new IllegalArgumentException("Especialidade já cadastrada.");
        return EspecialidadeResponse.from(repository.save(new Especialidade(req.nome(), req.descricao())));
    }
    @Transactional(readOnly = true)
    public List<EspecialidadeResponse> listar() { return repository.findAll().stream().map(EspecialidadeResponse::from).toList(); }
    private Especialidade obter(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada: " + id));
    }
}
