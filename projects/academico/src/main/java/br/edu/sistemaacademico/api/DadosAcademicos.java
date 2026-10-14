package br.edu.sistemaacademico.api;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DadosAcademicos {

    public final Map<String, Aluno> alunos = new LinkedHashMap<>();
    public final Map<String, Turma> turmas = new LinkedHashMap<>();
    public final Map<String, Matricula> matriculas = new LinkedHashMap<>();

    public DadosAcademicos() {
        Aluno paola = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola.oliveira@email.com"
        );

        Aluno bruno = new Aluno(
                "RA2026002",
                "Bruno Santos",
                "bruno.santos@email.com"
        );

        Disciplina poo = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        Disciplina bancoDados = new Disciplina(
                "BD",
                "Banco de Dados",
                80
        );

        Turma turma = new Turma(
                "ESOFT4S-NB",
                new PeriodoLetivo(2026, Semestre.PRIMEIRO)
        );

        var ofertaPoo = turma.ofertarDisciplina(poo);
        turma.ofertarDisciplina(bancoDados);

        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);
        turmas.put(turma.getCodigo(), turma);

        Matricula matricula = ofertaPoo.matricular("MAT-1001", paola);
        matriculas.put(matricula.getCodigo(), matricula);
    }
}
