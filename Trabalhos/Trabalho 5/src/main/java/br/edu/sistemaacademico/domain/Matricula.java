package br.edu.sistemaacademico.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_ra")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private OfertaDisciplina oferta;

    @Enumerated(EnumType.STRING)
    private ResultadoAcademico resultado;

    protected Matricula() {
    }

    public Matricula(Aluno aluno, OfertaDisciplina oferta) {
        this.aluno = aluno;
        this.oferta = oferta;
        this.resultado = ResultadoAcademico.CURSANDO;
    }

    public void concluir(ResultadoAcademico resultado) {
        if (resultado != ResultadoAcademico.APROVADO && resultado != ResultadoAcademico.REPROVADO) {
            throw new IllegalArgumentException("O resultado da conclusão deve ser APROVADO ou REPROVADO.");
        }
        if (this.resultado != ResultadoAcademico.CURSANDO) {
            throw new IllegalStateException("Somente matrículas em andamento podem ser concluídas.");
        }
        this.resultado = resultado;
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public ResultadoAcademico getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return "Matrícula: " + aluno.getNome() + " - " + oferta.getDisciplina().getNome();
    }
}
