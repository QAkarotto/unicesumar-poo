package br.edu.sistemaacademico.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oferta_disciplina")
public class OfertaDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @OneToMany(mappedBy = "ofertaDisciplina", fetch = FetchType.EAGER)
    private List<Matricula> matriculas = new ArrayList<>();

    protected OfertaDisciplina() {
    }

    public OfertaDisciplina(Turma turma, Disciplina disciplina) {
        this(disciplina.getCodigo() + "-" + turma.getPeriodoLetivo().getAno() + "-" + turma.getPeriodoLetivo().getSemestre().getNumero(), turma, disciplina);
    }

    public OfertaDisciplina(String codigo, Turma turma, Disciplina disciplina) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da oferta é obrigatório.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        this.codigo = codigo.trim();
        this.turma = turma;
        this.disciplina = disciplina;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Matricula matricular(Aluno aluno) {
        var codigoMatricula = "MAT-" + (matriculas.size() + 1);
        return matricular(codigoMatricula, aluno);
    }

    public Matricula matricular(String codigoMatricula, Aluno aluno) {
        return new Matricula(codigoMatricula, aluno, this);
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    public void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    public void removerMatricula(Matricula matricula) {
        matriculas.remove(matricula);
    }

    public void validarNovaMatricula(Aluno aluno) {
        boolean alunoJaMatriculado = matriculas.stream()
                .anyMatch(matricula -> matricula.getAluno().equals(aluno));

        if (alunoJaMatriculado) {
            throw new IllegalArgumentException(
                    "O aluno já está matriculado nesta oferta."
            );
        }
    }

    @Override
    public String toString() {
        return (codigo != null ? codigo : disciplina.getCodigo()) + " - " + turma;
    }
}
