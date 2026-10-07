package br.edu.sistemaacademico.config;

import br.edu.sistemaacademico.domain.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class Dados {
    private List<Aluno> alunos = new ArrayList<>();
    private List<Turma> turmas = new ArrayList<>();
    private List<Matricula> matriculas = new ArrayList<>();
    public Dados() {
        Aluno priscila = new Aluno("RA252912922", "Priscila Reksua", "priscila.reksua@email.com");
        Aluno nathaly = new Aluno("RA202600221", "Nathaly Pereira", "nathaly.pereira@email.com");
        alunos.add(priscila);
        alunos.add(nathaly);

        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma = new Turma("ESOFT4S-NB", periodo);

        OfertaDisciplina poo2026 = turma.ofertarDisciplina(poo);
        turmas.add(turma);

        Matricula m1 = poo2026.matricular(nathaly);
        matriculas.add(m1);
    }
    public List<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno buscarAlunoPorRa(String ra) {
        for (Aluno aluno : alunos) {
            if (aluno.getRa().equalsIgnoreCase(ra)) {
                return aluno;
            }
        }
        return null;
    }

    public Turma buscarTurmaPorCodigo(String codigo) {
        for (Turma turma : turmas) {
            if (turma.getCodigo().equalsIgnoreCase(codigo)) {
                return turma;
            }
        }
        return null;
    }

    public Matricula buscarMatriculaPorCodigo(String codigo) {
        for (Matricula m : matriculas) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                return m;
            }
        }
        return null;
    }

    public void salvarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }
}
