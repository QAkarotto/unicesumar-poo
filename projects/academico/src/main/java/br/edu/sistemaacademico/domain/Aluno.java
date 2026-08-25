package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        validarTexto(identificadorAcademico, "Identificador acadêmico");
        validarTexto(nome, "Nome");
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

    public void setNome(String nome) {
        validarTexto(nome, "Nome");
        this.nome = nome;
    }

    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    public void adicionarMatricula(Matricula matricula) {
        if (matricula != null && !this.matriculas.contains(matricula)) {
            this.matriculas.add(matricula);
        }
    }

    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas); // Protege o encapsulamento
    }

    // Verifica se o aluno já domina essa disciplina como o Goku domina o Instinto Superior.
    public boolean isAprovadoNaDisciplina(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(m -> m.getOferta().getDisciplina().getCodigo().equals(disciplina.getCodigo())
                        && m.getResultado() == ResultadoAcademico.APROVADO);
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio");
        }

        int arroba = email.indexOf("@");
        int ponto = email.lastIndexOf(".");

        if (arroba <= 0 || ponto <= arroba + 1 || ponto == email.length() - 1 || email.contains(" ")) {
            throw new IllegalArgumentException("E-mail inválido");
        }
    }

    @Override
    public String toString() {
        return nome + " (" + identificadorAcademico + ")";
    }
}