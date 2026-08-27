package br.edu.sistemaacademico.domain;

public class Matricula {
    private static int contagem = 1;
    private String codigo;
    private Aluno aluno;
    private OfertaDisciplina oferta;
    private ResultadoAcademico resultado;

    public Matricula(String codigo, Aluno aluno, OfertaDisciplina oferta) {
        if (codigo == null || codigo.equals("")) {
            throw new IllegalArgumentException("O código da matrícula é obrigatório.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("O aluno é obrigatório.");
        }
        if (oferta == null) {
            throw new IllegalArgumentException("Oferecer a disciplina é obrigatório.");
        }
        this.codigo = codigo;
        this.aluno = aluno;
        this.oferta = oferta;
    }
    public static int proximoNum(){
        return contagem++;
    }
    public void concluir(ResultadoAcademico resultado){
        if (resultado==null){
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Esta matrícula já foi concluída.");
        }
        this.resultado = resultado;
    }
    public String getCodigo() {
        return this.codigo;
    }
    public Aluno getAluno() {
        return this.aluno;
    }
    public OfertaDisciplina getOferta() {
        return this.oferta;
    }
    public ResultadoAcademico getResultado(){
        return this.resultado;
    }

    @Override
    public String toString() {
        return "Matrícula " + this.codigo + " | Aluno: " + this.aluno.getNome() + " | " + this.oferta + (this.resultado != null ? "EM CURSO" + this.resultado : "");
    }
}