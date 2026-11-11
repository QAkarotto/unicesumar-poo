package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.dto.CriarDisciplinaRequest;
import br.edu.sistemaacademico.dto.DisciplinaResponse;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegraDeNegocioException;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @Transactional
    public DisciplinaResponse cadastrar(CriarDisciplinaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        if (request.codigo() == null || request.codigo().isBlank()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        String codigo = request.codigo().trim();
        if (disciplinaRepository.existsByCodigo(codigo)) {
            throw new RegraDeNegocioException("Já existe uma disciplina com o código informado.");
        }

        Integer carga = request.obterCargaHoraria();
        if (carga == null) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }

        var disciplina = new Disciplina(codigo, request.nome(), carga);
        var salva = disciplinaRepository.save(disciplina);
        return DisciplinaResponse.de(salva);
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(Long id) {
        return disciplinaRepository.findById(id)
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da disciplina é obrigatório.");
        }
        return disciplinaRepository.findByCodigo(codigo.trim())
                .map(DisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> listarTodas() {
        return disciplinaRepository.findAll().stream()
                .map(DisciplinaResponse::de)
                .toList();
    }
}
