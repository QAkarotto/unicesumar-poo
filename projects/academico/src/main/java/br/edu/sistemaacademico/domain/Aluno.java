package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String identificador;
    private String nome;
    private String email;

    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(
            String identificador,
            String nome,
            String email
    ) {
        this.identificador = validarObrigatorio(
                identificador,
                "Identificador acadêmico"
        );

        this.nome = validarObrigatorio(
                nome,
                "Nome"
        );

        this.email = validarEmail(email);
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarObrigatorio(
                nome,
                "Nome"
        );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    public void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "Matrícula é obrigatória."
            );
        }

        if (matriculas.contains(matricula)) {
            throw new IllegalStateException(
                    "Matrícula já registrada no histórico."
            );
        }

        matriculas.add(matricula);
    }

    public boolean jaFoiAprovado(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        for (Matricula matricula : matriculas) {

            if (matricula.getOfertaDisciplina()
                    .getDisciplina()
                    .getCodigo()
                    .equals(disciplina.getCodigo())
                    && matricula.getResultado()
                    == ResultadoAcademico.APROVADO) {

                return true;
            }
        }

        return false;
    }

    private static String validarObrigatorio(
            String valor,
            String campo
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " é obrigatório."
            );
        }

        return valor.trim();
    }

    private static String validarEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "E-mail é obrigatório."
            );
        }

        String emailNormalizado = email.trim();

        if (!emailNormalizado.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        )) {
            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }

        return emailNormalizado;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "identificador='" + identificador + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}