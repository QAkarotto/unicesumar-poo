package br.edu.sistemaacademico.domain;


public class Matricula {

    private final Aluno aluno;
    private final OfertaDisciplina ofertaDisciplina;
    private ResultadoAcademico resultado;

    Matricula(Aluno aluno, OfertaDisciplina ofertaDisciplina) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno da matrícula é obrigatório.");
        }
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("Oferta de disciplina da matrícula é obrigatória.");
        }

        this.aluno = aluno;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

   
    public ResultadoAcademico getResultado() {
        return resultado;
    }

    
    public void concluir(ResultadoAcademico resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado da matrícula é obrigatório.");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Matrícula já foi concluída e não pode ser alterada.");
        }

        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "aluno='" + aluno.getNome() + '\'' +
                ", oferta=" + ofertaDisciplina +
                ", resultado=" + (resultado == null ? "EM_ANDAMENTO" : resultado) +
                '}';
    }
}
