package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegraDeNegocioException;
import br.edu.sistemaacademico.repository.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public Turma criarTurma(String codigo, int ano, Semestre semestre) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        String cod = codigo.trim();
        if (turmaRepository.existsByCodigo(cod)) {
            throw new RegraDeNegocioException("Turma já cadastrada com este código.");
        }
        var turma = new Turma(cod, new PeriodoLetivo(ano, semestre));
        return turmaRepository.save(turma);
    }

    @Transactional(readOnly = true)
    public Turma buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        return turmaRepository.findByCodigo(codigo.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<Turma> listarTodas() {
        return turmaRepository.findAll();
    }
}
