package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Guarda em memória as turmas e matrículas. Alunos e disciplinas vêm do banco,
 * por meio dos repositories (os dados iniciais ficam em database/dados-iniciais.sql).
 */
@Component
public class DadosEmMemoria {

    private final AlunoRepository alunoRepository;
    private final Map<String, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<String, Turma> turmas = new ConcurrentHashMap<>();
    private final Map<String, Matricula> matriculas = new ConcurrentHashMap<>();
    private final AtomicInteger sequenciaMatricula = new AtomicInteger();

    public DadosEmMemoria(AlunoRepository alunoRepository,
                          DisciplinaRepository disciplinaRepository) {
        this.alunoRepository = alunoRepository;

        var paola = buscarAluno("RA2026001").orElseThrow(DadosEmMemoria::dadosIniciaisAusentes);
        var bruno = buscarAluno("RA2026002").orElseThrow(DadosEmMemoria::dadosIniciaisAusentes);

        var poo = disciplinaRepository.buscarPorCodigo("POO")
                .orElseThrow(DadosEmMemoria::dadosIniciaisAusentes);
        var bancoDados = disciplinaRepository.buscarPorCodigo("BD")
                .orElseThrow(DadosEmMemoria::dadosIniciaisAusentes);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026 = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turma2025.ofertarDisciplina(poo);
        turma2026.ofertarDisciplina(poo);
        turma2026.ofertarDisciplina(bancoDados);
        turmas.put(turma2025.getCodigo(), turma2025);
        turmas.put(turma2026.getCodigo(), turma2026);

        // MAT-001: matrícula ativa do Bruno em POO (ADSIS4S).
        var oferta2026 = turma2026.getOfertas().getFirst();
        salvar(oferta2026.matricular(proximoCodigo(), bruno));

        // MAT-002: Paola já foi aprovada em POO, então não pode se matricular de novo.
        var oferta2025 = turma2025.getOfertas().getFirst();
        var aprovada = oferta2025.matricular(proximoCodigo(), paola);
        aprovada.concluir(ResultadoAcademico.APROVADO);
        salvar(aprovada);
    }

    /**
     * Devolve o aluno usado nas regras de matrícula. Como as matrículas ficam em memória
     * e referenciam o objeto Aluno, o objeto lido do banco é reaproveitado enquanto a
     * aplicação está no ar.
     */
    public Optional<Aluno> buscarAluno(String registroAcademico) {
        var emMemoria = alunos.get(registroAcademico);
        if (emMemoria != null) {
            return Optional.of(emMemoria);
        }
        var doBanco = alunoRepository.buscarPorRa(registroAcademico);
        doBanco.ifPresent(aluno -> alunos.putIfAbsent(aluno.getRegistroAcademico(), aluno));
        return doBanco;
    }

    public List<Matricula> matriculasDoAluno(String registroAcademico) {
        return matriculas.values().stream()
                .filter(m -> m.getAluno().getRegistroAcademico().equals(registroAcademico))
                .sorted(java.util.Comparator.comparing(Matricula::getCodigo))
                .toList();
    }

    public Optional<Turma> buscarTurma(String codigo) {
        return Optional.ofNullable(turmas.get(codigo));
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    public boolean existeMatricula(String codigo) {
        return matriculas.containsKey(codigo);
    }

    public void salvar(Matricula matricula) {
        matriculas.put(matricula.getCodigo(), matricula);
    }

    public String proximoCodigo() {
        return String.format("MAT-%03d", sequenciaMatricula.incrementAndGet());
    }

    private static IllegalStateException dadosIniciaisAusentes() {
        return new IllegalStateException(
                "Dados iniciais ausentes no banco. Execute database/dados-iniciais.sql.");
    }
}
