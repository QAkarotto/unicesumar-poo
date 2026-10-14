package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Guarda os objetos do domínio apenas em memória (sem banco de dados).
 * Os dados iniciais são carregados no construtor.
 */
@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<String, Turma> turmas = new ConcurrentHashMap<>();
    private final Map<String, Matricula> matriculas = new ConcurrentHashMap<>();
    private final AtomicInteger sequenciaMatricula = new AtomicInteger();

    public DadosEmMemoria() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

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

    public Optional<Aluno> buscarAluno(String registroAcademico) {
        return Optional.ofNullable(alunos.get(registroAcademico));
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
}
