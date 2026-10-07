package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.ResultadoAcademico;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new LinkedHashMap<>();
    private final Map<String, Turma> turmas = new LinkedHashMap<>();
    private final Map<String, Matricula> matriculas = new LinkedHashMap<>();

    public DadosEmMemoria() {
        var paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        var bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");
        alunos.put(paola.getRa(), paola);
        alunos.put(bruno.getRa(), bruno);

        var poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        var bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        var turma2025 = new Turma("ESOFT4S-NA", new PeriodoLetivo(2025, Semestre.SEGUNDO));
        var turma2026A = new Turma("ESOFT4S-NB", new PeriodoLetivo(2026, Semestre.PRIMEIRO));
        var turma2026B = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        turmas.put(turma2025.getCodigo(), turma2025);
        turmas.put(turma2026A.getCodigo(), turma2026A);
        turmas.put(turma2026B.getCodigo(), turma2026B);

        var poo2025 = turma2025.ofertarDisciplina(poo);
        var poo2026A = turma2026A.ofertarDisciplina(poo);
        turma2026A.ofertarDisciplina(bancoDados);
        turma2026B.ofertarDisciplina(poo);

        var primeira = poo2025.matricular(paola);
        primeira.concluir(ResultadoAcademico.REPROVADO);
        registrar(primeira);

        var segunda = poo2026A.matricular(paola);
        segunda.concluir(ResultadoAcademico.APROVADO);
        registrar(segunda);

        registrar(poo2026A.matricular(bruno));
    }

    public Aluno buscarAluno(String ra) {
        return alunos.get(ra);
    }

    public Turma buscarTurma(String codigo) {
        return turmas.get(codigo);
    }

    public Matricula buscarMatricula(String id) {
        return matriculas.get(id);
    }

    public String registrar(Matricula matricula) {
        String id = String.format("MAT-%03d", matriculas.size() + 1);
        matriculas.put(id, matricula);
        return id;
    }
}