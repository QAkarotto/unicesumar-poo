package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final Map<String, Disciplina> disciplinas = new HashMap<>();
    private final Map<String, Turma> turmas = new HashMap<>();
    private final Map<String, Matricula> matriculas = new HashMap<>();
    private int sequenciaMatricula = 1;

    public DadosEmMemoria() {
        Aluno pedro = new Aluno("RA2026101", "Pedro Henrique", "pedro@example.com");
        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        Disciplina bd = new Disciplina("BD", "Banco de Dados", 80);
        Turma turmaPoo = new Turma("POO-NA", poo, new PeriodoLetivo(2026, Semestre.SEGUNDO));
        Turma turmaBd = new Turma("BD-NB", bd, new PeriodoLetivo(2026, Semestre.SEGUNDO));

        alunos.put(pedro.getRegistroAcademico(), pedro);
        disciplinas.put(poo.getCodigo(), poo);
        disciplinas.put(bd.getCodigo(), bd);
        turmas.put(turmaPoo.getCodigo(), turmaPoo);
        turmas.put(turmaBd.getCodigo(), turmaBd);

        // Matrícula existente para testar GET /api/matriculas/MAT-001
        Matricula mat001 = new Matricula("MAT-001", pedro, turmaPoo);
        matriculas.put(mat001.getCodigo(), mat001);
    }

    public Optional<Aluno> buscarAluno(String ra) {
        return Optional.ofNullable(alunos.get(ra));
    }

    public Optional<Disciplina> buscarDisciplina(String codigo) {
        return Optional.ofNullable(disciplinas.get(codigo));
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.put(disciplina.getCodigo(), disciplina);
    }

    public Optional<Turma> buscarTurma(String codigo) {
        return Optional.ofNullable(turmas.get(codigo));
    }

    public Optional<Matricula> buscarMatricula(String codigo) {
        return Optional.ofNullable(matriculas.get(codigo));
    }

    public void adicionarMatricula(Matricula matricula) {
        matriculas.put(matricula.getCodigo(), matricula);
    }

    public String proximoCodigoMatricula() {
        return String.format("MAT-%03d", sequenciaMatricula++);
    }
}