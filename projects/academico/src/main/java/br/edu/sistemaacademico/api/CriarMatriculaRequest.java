package br.edu.sistemaacademico.api;

public class CriarMatriculaRequest {
    private String codigo;
    private String identificadorAluno;
    private String codigoTurma;

    public CriarMatriculaRequest() {}

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getIdentificadorAluno() { return identificadorAluno; }
    public void setIdentificadorAluno(String identificadorAluno) { this.identificadorAluno = identificadorAluno; }
    public String getCodigoTurma() { return codigoTurma; }
    public void setCodigoTurma(String codigoTurma) { this.codigoTurma = codigoTurma; }
}
