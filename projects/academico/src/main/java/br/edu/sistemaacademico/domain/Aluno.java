package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    private final List<Matricula> historico;

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTexto(
                identificadorAcademico,
                "O identificador acadêmico é obrigatório."
        );

        validarTexto(
                nome,
                "O nome do aluno é obrigatório."
        );

        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
        this.historico = new ArrayList<>();
    }

    public String getIdentificadorAcademico() {
        return identificadorAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void alterarNome(String nome) {
        validarTexto(
                nome,
                "O nome do aluno é obrigatório."
        );

        this.nome = nome;
    }

    public void alterarEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula."
            );
        }

        historico.add(matricula);
    }

    public boolean jaFoiAprovado(Disciplina disciplina) {

        for (Matricula matricula : historico) {

            if (matricula.getOferta()
                    .getDisciplina()
                    .getCodigo()
                    .equals(disciplina.getCodigo())
                    &&
                    matricula.getResultado() == Resultado.APROVADO) {

                return true;
            }
        }

        return false;
    }

    public List<Matricula> getHistorico() {
        return historico;
    }

    private void validarTexto(String valor, String mensagem) {

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private void validarEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "O e-mail é obrigatório."
            );
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException(
                    "O e-mail informado é inválido."
            );
        }
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}