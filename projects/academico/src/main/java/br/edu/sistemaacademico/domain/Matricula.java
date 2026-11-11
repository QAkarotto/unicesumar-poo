package br.edu.sistemaacademico.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aluno_ra", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oferta_id", nullable = false)
    private OfertaDisciplina ofertaDisciplina;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 30, nullable = false)
    private SituacaoMatricula situacao = SituacaoMatricula.ATIVA;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", length = 30)
    private ResultadoAcademico resultadoAcademico;

    protected Matricula() {
    }

    public Matricula(String codigo, Aluno aluno, Turma turma) {
        this(codigo, aluno, obterOfertaDaTurma(turma));
    }

    public Matricula(String codigo, Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("A oferta da disciplina é obrigatória.");
        }

        ofertaDisciplina.validarNovaMatricula(aluno);
        aluno.validarNovaMatricula(ofertaDisciplina);

        this.codigo = codigo.trim();
        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;

        ofertaDisciplina.registrarMatricula(this);
        aluno.registrarMatricula(this);
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public OfertaDisciplina getOferta() {
        return ofertaDisciplina;
    }

    public Turma getTurma() {
        return ofertaDisciplina.getTurma();
    }

    public SituacaoMatricula getSituacao() {
        return situacao;
    }

    public ResultadoAcademico getResultado() {
        return resultadoAcademico;
    }

    public void concluir(ResultadoAcademico resultadoAcademico) {
        exigirSituacaoAtiva("concluir");
        if (resultadoAcademico == null) {
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório.");
        }

        this.resultadoAcademico = resultadoAcademico;
        this.situacao = SituacaoMatricula.CONCLUIDA;
    }

    public void trancar() {
        exigirSituacaoAtiva("trancar");
        situacao = SituacaoMatricula.TRANCADA;
    }

    public void cancelar() {
        exigirSituacaoAtiva("cancelar");
        situacao = SituacaoMatricula.CANCELADA;
    }

    public boolean foiAprovadoEm(Disciplina disciplina) {
        return resultadoAcademico == ResultadoAcademico.APROVADO
                && ofertaDisciplina.getDisciplina().equals(disciplina);
    }

    private void exigirSituacaoAtiva(String operacao) {
        if (situacao != SituacaoMatricula.ATIVA) {
            throw new IllegalStateException(
                    "Não é possível " + operacao + " uma matrícula " + situacao + "."
            );
        }
    }

    private static OfertaDisciplina obterOfertaDaTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("A turma é obrigatória.");
        }
        return turma.obterUnicaOferta();
    }

    @Override
    public String toString() {
        return codigo + " - " + aluno.getRegistroAcademico()
                + " - " + ofertaDisciplina.getDisciplina().getCodigo()
                + " - " + situacao;
    }
}
