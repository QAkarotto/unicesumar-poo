package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    private boolean isEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    // Getters
    public String getIdentificadorAcademico() { return identificadorAcademico; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    // Setters com validação
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }



    @JsonIgnore
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula não pode ser nula.");
        }
        this.matriculas.add(matricula);
    }

    public boolean jaAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            return false;
        }
        return matriculas.stream()
                .anyMatch(m -> m.getResultado() == ResultadoAcademico.APROVADO
                        && disciplina.equals(m.getOfertaDisciplina().getDisciplina()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno aluno)) return false;
        return identificadorAcademico.equals(aluno.identificadorAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico);
    }

    @Override
    public String toString() {
        return "Aluno: " + identificadorAcademico + " - " + nome + " (" + email + ")";
    }

    public static class Disciplina {

        private final String codigo;
        private final String nome;
        private final int cargaHoraria;

        public Disciplina(String codigo, String nome, int cargaHoraria) {
            if (codigo == null || codigo.isBlank()) {
                throw new IllegalArgumentException(
                        "O código da disciplina não pode ser vazio."
                );
            }
            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException(
                        "O nome da disciplina não pode ser vazio."
                );
            }
            if (cargaHoraria <= 0) {
                throw new IllegalArgumentException(
                        "A carga horária deve ser maior que zero."
                );
            }
            this.codigo = codigo;
            this.nome = nome;
            this.cargaHoraria = cargaHoraria;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getNome() {
            return nome;
        }

        public int getCargaHoraria() {
            return cargaHoraria;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Disciplina that)) return false;
            return codigo.equals(that.codigo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(codigo);
        }

        @Override
        public String toString() {
            return "Disciplina{" +
                    "codigo='" + codigo + '\'' +
                    ", nome='" + nome + '\'' +
                    ", cargaHoraria=" + cargaHoraria +
                    '}';
        }
    }
}
