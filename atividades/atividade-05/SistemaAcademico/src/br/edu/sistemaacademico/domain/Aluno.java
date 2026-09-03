package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {
    private final String id;
    private final String nome;
    private String email;
    private final List<MatriculaDisciplina> historico;

    public Aluno(String id, String nome, String email) {
        validarId(id);
        validarNome(nome);
        validarEmail(email);

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.historico = new ArrayList<>();
    }

    private void validarId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do aluno não pode ser vazio");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email do aluno não pode ser vazio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void alterarEmail(String novoEmail) {
        validarEmail(novoEmail);
        this.email = novoEmail;
    }

    public void adicionarMatricula(MatriculaDisciplina matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("Matrícula não pode ser nula");
        }

        if (jaFoiAprovadoNaDisciplina(matricula.getOferta().getDisciplina())) {
            throw new IllegalStateException(
                "Aluno " + this.nome +
                " já foi aprovado na disciplina " + matricula.getOferta().getDisciplina().getNome() +
                " e não pode realizar nova matrícula"
            );
        }

        historico.add(matricula);
    }

    private boolean jaFoiAprovadoNaDisciplina(Disciplina disciplina) {
        return historico.stream()
                .anyMatch(m -> m.getOferta().getDisciplina().getCodigo().equals(disciplina.getCodigo()) &&
                        m.getResultado() == ResultadoMatricula.APROVADO);
    }

    public List<MatriculaDisciplina> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public boolean possuiAprovacaoNaDisciplina(Disciplina disciplina) {
        return jaFoiAprovadoNaDisciplina(disciplina);
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
