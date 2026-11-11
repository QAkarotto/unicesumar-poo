package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.dto.CriarOfertaRequest;
import br.edu.sistemaacademico.dto.OfertaDisciplinaResponse;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegraDeNegocioException;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import br.edu.sistemaacademico.repository.OfertaDisciplinaRepository;
import br.edu.sistemaacademico.repository.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfertaDisciplinaService {

    private final OfertaDisciplinaRepository ofertaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TurmaRepository turmaRepository;

    public OfertaDisciplinaService(
            OfertaDisciplinaRepository ofertaRepository,
            DisciplinaRepository disciplinaRepository,
            TurmaRepository turmaRepository) {
        this.ofertaRepository = ofertaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public OfertaDisciplinaResponse criarOferta(CriarOfertaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        var disciplina = disciplinaRepository.findByCodigo(request.codigoDisciplina())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada."));

        var turma = turmaRepository.findByCodigo(request.codigoTurma())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));

        String codigo = (request.codigo() != null && !request.codigo().isBlank())
                ? request.codigo().trim()
                : disciplina.getCodigo() + "-" + turma.getPeriodoLetivo().getAno() + "-" + turma.getPeriodoLetivo().getSemestre().getNumero();

        if (ofertaRepository.existsByCodigo(codigo)) {
            throw new RegraDeNegocioException("Já existe uma oferta com este código.");
        }

        var oferta = new OfertaDisciplina(codigo, turma, disciplina);
        var salva = ofertaRepository.save(oferta);
        return OfertaDisciplinaResponse.de(salva);
    }

    @Transactional(readOnly = true)
    public OfertaDisciplinaResponse buscarPorId(Long id) {
        return ofertaRepository.findById(id)
                .map(OfertaDisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta não encontrada."));
    }

    @Transactional(readOnly = true)
    public OfertaDisciplinaResponse buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código da oferta é obrigatório.");
        }
        return ofertaRepository.findByCodigo(codigo.trim())
                .map(OfertaDisciplinaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<OfertaDisciplinaResponse> listarTodas() {
        return ofertaRepository.findAll().stream()
                .map(OfertaDisciplinaResponse::de)
                .toList();
    }
}
