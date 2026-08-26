package br.edu.sistemaacademico.domain;

public class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    Matricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }

        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }

        if (oferta == null) {
            throw new IllegalArgumentException("A oferta de disciplina é obrigatória.");
        }

        this.codigo = codigo;
        this.aluno = aluno;
        this.oferta = oferta;
    }

    public String getCodigo() {
        return codigo;
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

    //A matrícula é quem controla o seu próprio estado de conclusão, impedindo alterações inválidas.


    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado é obrigatório.");
        }

        if (this.resultado != null) {
            throw new IllegalStateException(
                    "A matrícula " + codigo + " já foi concluída anteriormente."
            );
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Código: " + getCodigo() +
                "\nAluno: " + aluno.getNome() +
                "\nDisciplina: " + oferta.getDisciplina().getNome() +
                "\nTurma: " + oferta.getTurma().getCodigo() +
                //operador ternário
                "\nResultado: " + (resultado != null ? resultado : "Em andamento");
    }
}
