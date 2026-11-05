package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.dto.CriarDisciplinaRequest;
import br.edu.sistemaacademico.dto.DisciplinaResponse;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegistroDuplicadoException;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public DisciplinaResponse cadastrar(CriarDisciplinaRequest request) {
        Disciplina disciplina = new Disciplina(request.codigo(), request.nome(), request.cargaHoraria());

        if (disciplinaRepository.existsById(disciplina.getCodigo())) {
            throw new RegistroDuplicadoException("Já existe disciplina com o código: " + disciplina.getCodigo());
        }

        return DisciplinaResponse.de(disciplinaRepository.save(disciplina));
    }

    public List<DisciplinaResponse> listar() {
        return disciplinaRepository.findAll().stream().map(DisciplinaResponse::de).toList();
    }

    public DisciplinaResponse buscarPorCodigo(String codigo) {
        return disciplinaRepository.findById(codigo)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada: " + codigo));
    }
}
