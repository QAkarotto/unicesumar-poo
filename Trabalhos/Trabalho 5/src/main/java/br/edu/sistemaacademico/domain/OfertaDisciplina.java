package br.edu.sistemaacademico.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oferta_disciplina")
public class OfertaDisciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turma_codigo")
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "disciplina_codigo")
    private Disciplina disciplina;

    @OneToMany(mappedBy = "oferta")
    private List<Matricula> matriculas = new ArrayList<>();

    protected OfertaDisciplina() {
    }

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Long getId() {
        return id;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public Matricula matricular(Aluno aluno) {
        if (aluno.jaFoiAprovadoEm(this.disciplina)) {
            throw new IllegalStateException("Aluno " + aluno.getNome() + " já foi aprovado na disciplina " + disciplina.getNome() + ".");
        }

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno)) {
                throw new IllegalStateException("Aluno " + aluno.getNome() + " já está matriculado nesta oferta.");
            }
        }

        Matricula novaMatricula = new Matricula(aluno, this);
        this.matriculas.add(novaMatricula);
        aluno.getMatriculas().add(novaMatricula);
        return novaMatricula;
    }

    @Override
    public String toString() {
        return disciplina.getNome();
    }
}
