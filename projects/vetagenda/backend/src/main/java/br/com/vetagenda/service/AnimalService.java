package br.com.vetagenda.service;

import br.com.vetagenda.domain.Animal;
import br.com.vetagenda.dto.requests.AnimalRequest;
import br.com.vetagenda.dto.responses.AnimalResponse;
import br.com.vetagenda.exception.RecursoNaoEncontradoException;
import br.com.vetagenda.repository.AnimalRepository;
import br.com.vetagenda.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AnimalService {
    private final AnimalRepository animais;
    private final TutorRepository tutores;
    public AnimalService(AnimalRepository animais, TutorRepository tutores) {
        this.animais = animais; this.tutores = tutores;
    }
    @Transactional
    public AnimalResponse cadastrar(AnimalRequest req) {
        var tutor = tutores.findById(req.tutorId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor não encontrado."));
        return AnimalResponse.from(animais.save(new Animal(req.nome(), req.especie(), req.raca(),
            req.sexo(), req.dataNascimento(), req.peso(), tutor)));
    }
    @Transactional(readOnly = true)
    public List<AnimalResponse> listar() { return animais.findAll().stream().map(AnimalResponse::from).toList(); }
    @Transactional(readOnly = true)
    public AnimalResponse buscar(Long id) { return AnimalResponse.from(obter(id)); }
    @Transactional
    public AnimalResponse atualizar(Long id, AnimalRequest req) {
        var animal = obter(id);
        var tutor = tutores.findById(req.tutorId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor não encontrado."));
        animal.atualizar(req.nome(), req.especie(), req.raca(), req.sexo(),
            req.dataNascimento(), req.peso(), tutor);
        return AnimalResponse.from(animal);
    }
    @Transactional
    public void excluir(Long id) { animais.delete(obter(id)); }
    private Animal obter(Long id) {
        return animais.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Animal não encontrado: " + id));
    }
}
