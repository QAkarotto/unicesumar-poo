package br.edu.sistemaacademico.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.domain.PeriodoLetivo;
import br.edu.sistemaacademico.domain.Semestre;
import br.edu.sistemaacademico.domain.Turma;

@Component
public class DadosEmMemoria {

    private final Map<String, Aluno> alunos = new HashMap<>();
    private final OfertaDisciplina poo;

    public DadosEmMemoria() {
        Aluno paola = new Aluno("RA2026001", "Paola Oliveira", "paola@email.com");
        Aluno bruno = new Aluno("RA2026002", "Bruno Santos", "bruno@email.com");
        alunos.put(paola.getRegistroAcademico(), paola);
        alunos.put(bruno.getRegistroAcademico(), bruno);

        Turma turma = new Turma("ADSIS4S", new PeriodoLetivo(2026, Semestre.SEGUNDO));
        poo = turma.ofertarDisciplina(new Disciplina("POO", "Programação Orientada a Objetos", 80));
        poo.matricular(paola);
    }

    public Aluno buscarAluno(String ra) {
        return alunos.get(ra);
    }

    public Matricula buscarMatricula(String codigo) {
        for (Matricula matricula : poo.getMatriculas()) {
            if (matricula.getCodigo().equals(codigo)) {
                return matricula;
            }
        }
        return null;
    }

    public OfertaDisciplina getPoo() {
        return poo;
    }
}