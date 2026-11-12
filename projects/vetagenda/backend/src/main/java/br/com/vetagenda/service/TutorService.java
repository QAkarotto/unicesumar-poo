package br.com.vetagenda.service;

import br.com.vetagenda.domain.Tutor;
import br.com.vetagenda.dto.requests.TutorRequest;
import br.com.vetagenda.dto.responses.TutorResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TutorService {
    private final TutorRepository repository;
    public TutorService(TutorRepository repository) { this.repository = repository; }

    @Transactional
    public TutorResponse cadastrar(TutorRequest req) {
        if (repository.existsByCpf(req.cpf())) throw new IllegalArgumentException("Já existe tutor com esse CPF.");
        return TutorResponse.from(repository.save(new Tutor(req.nome(), req.cpf(), req.telefone(), req.email())));
    }
    @Transactional(readOnly = true)
    public List<TutorResponse> listar() { return repository.findAll().stream().map(TutorResponse::from).toList(); }
    @Transactional(readOnly = true)
    public TutorResponse buscar(Long id) { return TutorResponse.from(obter(id)); }
    @Transactional
    public TutorResponse atualizar(Long id, TutorRequest req) {
        Tutor t = obter(id);
        t.atualizar(req.nome(), req.telefone(), req.email());
        return TutorResponse.from(t);
    }
    @Transactional
    public void excluir(Long id) { repository.delete(obter(id)); }
    private Tutor obter(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Tutor não encontrado: " + id));
    }
}
