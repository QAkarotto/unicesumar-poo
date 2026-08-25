package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aluno {
    private final String identificadorAcademico;
    private final String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = validarTexto(identificadorAcademico, "Identificador acadêmico");
        this.nome = validarTexto(nome, "Nome do aluno");
        alterarEmail(email);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
        return valor.trim();
    }

    public void alterarEmail(String novoEmail) {
        if (novoEmail == null || novoEmail.isBlank() || !novoEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = novoEmail.trim();
    }

    public boolean foiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        return matriculas.stream().anyMatch(matricula ->
                matricula.getOferta().getDisciplina().equals(disciplina)
                        && matricula.getResultado() == ResultadoAcademico.APROVADO
        );
    }

    void registrarMatricula(Matricula matricula) {
        if (matricula == null || !this.equals(matricula.getAluno())) {
            throw new IllegalArgumentException("A matrícula não pertence a este aluno.");
        }
        matriculas.add(matricula);
    }

    public String getIdentificadorAcademico() { return identificadorAcademico; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public List<Matricula> getMatriculas() { return List.copyOf(matriculas); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Aluno outro)) return false;
        return identificadorAcademico.equalsIgnoreCase(outro.identificadorAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico.toUpperCase());
    }

    @Override
    public String toString() {
        return identificadorAcademico + " - " + nome;
    }
}
