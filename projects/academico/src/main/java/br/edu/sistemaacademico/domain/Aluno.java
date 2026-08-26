package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {
    private final String ra;
    private String nome;
    private String email;
    private final List<Matricula> historico;

    public Aluno(String ra, String nome, String email) {
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("O RA do aluno é obrigatório e não pode ser nulo ou vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório e não pode ser nulo ou vazio.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail do aluno é obrigatório e não pode ser nulo ou vazio.");
        }

        this.ra = ra.trim();
        this.nome = nome.trim();
        this.email = email.trim();
        this.historico = new ArrayList<>();
    }

    public String getRa() {
        return ra;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void alterarDados(String novoNome, String novoEmail) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O novo nome não pode ser nulo ou vazio.");
        }
        if (novoEmail == null || novoEmail.isBlank()) {
            throw new IllegalArgumentException("O novo e-mail não pode ser nulo ou vazio.");
        }
        this.nome = novoNome.trim();
        this.email = novoEmail.trim();
    }

    void adicionarMatricula(Matricula matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException( "O objeto matrícula não pode ser nulo.");
}

 historico.add(matricula);
    }
     public List<Matricula> getHistorico() {
        return Collections.unmodifiableList(historico);
    }
  public boolean foiAprovadoEm(Disciplina disciplina) {
        if (disciplina == null) {
            return false;
        }
        return historico.stream()
                .anyMatch(m -> m.getOfertaDisciplina().getDisciplina().equals(disciplina) 
                        && m.isConcluida());
    }
}