package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String identificadorAcademico;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTextoObrigatorio(
                identificadorAcademico,
                "O identificador acadêmico é obrigatório."
        );

        validarTextoObrigatorio(
                nome,
                "O nome é obrigatório."
        );

        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
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

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula é obrigatória."
            );
        }

        if (!matriculas.contains(matricula)) {
            matriculas.add(matricula);
        }
    }

    public boolean possuiAprovacaoNaDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina é obrigatória."
            );
        }

        for (Matricula matricula : matriculas) {
            if (matricula.getResultado() == ResultadoAcademico.APROVADO
                    && matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .getCodigo()
                    .equals(disciplina.getCodigo())) {

                return true;
            }
        }

        return false;
    }

    private static void validarTextoObrigatorio(
            String valor,
            String mensagem
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private static void validarEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException(
                    "O e-mail deve ser válido."
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