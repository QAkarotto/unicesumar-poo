package br.edu.sistemaacademico.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DadosEmMemoria {

    private final List<Aluno> alunos = new ArrayList<>();
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();
    private final List<Matricula> matriculas = new ArrayList<>();

    public DadosEmMemoria() {
        Aluno paola = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
        Aluno bruno = new Aluno("RA2026002", "Bruno Santos", "bruno.santos@email.com");

        alunos.add(paola);
        alunos.add(bruno);

        Disciplina poo = new Disciplina("POO", "Programação Orientada a Objetos", 80);
        Disciplina bancoDados = new Disciplina("BD", "Banco de Dados", 80);

        PeriodoLetivo periodo2026_1 = new PeriodoLetivo(2026, Semestre.PRIMEIRO);
        Turma turma2026A = new Turma("ESOFT4S-NB", periodo2026_1);

        OfertaDisciplina poo2026A = turma2026A.ofertarDisciplina(poo);
        OfertaDisciplina bd2026A = turma2026A.ofertarDisciplina(bancoDados);

        ofertas.add(poo2026A);
        ofertas.add(bd2026A);

        Matricula m1 = poo2026A.matricular(bruno);
        matriculas.add(m1);
    }

    public Aluno buscarAlunoPorRa(String ra) {
        return alunos.stream()
                .filter(a -> a.getRa().equalsIgnoreCase(ra))
                .findFirst()
                .orElse(null);
    }

    public Matricula buscarMatriculaPorId(String id) {
        return matriculas.stream()
                .filter(m -> m.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public Matricula realizarMatricula(String raAluno, String codigoOferta) {
        Aluno aluno = buscarAlunoPorRa(raAluno);
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado com o RA: " + raAluno);
        }

        OfertaDisciplina oferta = ofertas.stream()
                .filter(o -> o.getCodigo().equalsIgnoreCase(codigoOferta))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Oferta de disciplina não encontrada: " + codigoOferta));

        Matricula novaMatricula = oferta.matricular(aluno);
        matriculas.add(novaMatricula);

        return novaMatricula;
    }
}