package br.edu.sistemaacademico.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @Column(name = "registro_academico", length = 30, nullable = false)
    private String registroAcademico;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @OneToMany(mappedBy = "aluno", fetch = FetchType.EAGER)
    private List<Matricula> matriculas = new ArrayList<>();

    protected Aluno() {
    }

    public Aluno(String registroAcademico, String nome, String email) {
        this.registroAcademico = validarTexto(
                registroAcademico,
                "O registro acadêmico é obrigatório."
        );
        this.nome = validarTexto(nome, "O nome do aluno é obrigatório.");
        alterarEmail(email);
    }

    public String getRegistroAcademico() {
        return registroAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        alterarEmail(email);
    }

    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    public void validarNovaMatricula(OfertaDisciplina oferta) {
        boolean jaAprovado = matriculas.stream()
                .anyMatch(matricula -> matricula.foiAprovadoEm(oferta.getDisciplina()));

        if (jaAprovado) {
            throw new IllegalStateException(
                    "O aluno já foi aprovado nesta disciplina."
            );
        }
    }

    public void registrarMatricula(Matricula matricula) {
        matriculas.add(matricula);
    }

    public void removerMatricula(Matricula matricula) {
        matriculas.remove(matricula);
    }

    private void alterarEmail(String email) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("O e-mail do aluno é inválido.");
        }
        this.email = email.trim();
    }

    private static String validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Aluno aluno)) {
            return false;
        }
        return Objects.equals(registroAcademico, aluno.registroAcademico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registroAcademico);
    }

    @Override
    public String toString() {
        return registroAcademico + " - " + nome;
    }
}
