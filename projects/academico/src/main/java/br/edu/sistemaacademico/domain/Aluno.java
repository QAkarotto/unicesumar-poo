package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Representa um aluno dentro do sistema acadêmico.
public class Aluno {

    // Identificador único do aluno no sistema.
    private final String identificadorAcademico;

    // Dados pessoais e de contato do aluno.
    private String nome;
    private String email;

    // Lista de matrículas vinculadas ao aluno.
    private final List<Matricula> matriculas = new ArrayList<>();

    // Cria um novo aluno realizando as validações dos dados obrigatórios.
    public Aluno(String identificadorAcademico, String nome, String email) {

        // Verifica se o identificador acadêmico foi informado.
        if (identificadorAcademico == null || identificadorAcademico.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }

        // Verifica se o nome foi informado.
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        // Valida o formato básico do e-mail.
        validarEmail(email);

        // Inicializa os dados do aluno.
        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    // Retorna o identificador acadêmico do aluno.
    public String getIdentificadorAcademico() {
        return identificadorAcademico;
    }

    // Retorna o nome do aluno.
    public String getNome() {
        return nome;
    }

    // Retorna o e-mail do aluno.
    public String getEmail() {
        return email;
    }

    // Altera o nome do aluno após validar se o valor foi informado.
    public void alterarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        this.nome = nome;
    }

    // Altera o e-mail do aluno após realizar sua validação.
    public void alterarEmail(String email) {
        validarEmail(email);
        this.email = email;
    }

    // Retorna as matrículas do aluno em uma lista somente para leitura.
    public List<Matricula> getMatriculas() {
        return Collections.unmodifiableList(matriculas);
    }

    // Adiciona uma nova matrícula à lista do aluno.
    void adicionarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    // Verifica se o aluno já foi aprovado na disciplina informada.
    boolean possuiAprovacaoEm(Disciplina disciplina) {
        return matriculas.stream()
                .anyMatch(m -> m.getDisciplina().equals(disciplina)
                        && m.getResultado() == ResultadoAcademico.APROVADO);
    }

    // Realiza uma validação básica do endereço de e-mail.
    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }

        // Verifica se o e-mail possui os caracteres básicos esperados.
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    // Compara alunos utilizando o identificador acadêmico como identidade.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;

        Aluno aluno = (Aluno) o;

        return identificadorAcademico.equals(aluno.identificadorAcademico);
    }

    // Gera o código hash utilizando o identificador acadêmico.
    @Override
    public int hashCode() {
        return Objects.hash(identificadorAcademico);
    }

    // Retorna uma representação textual dos dados principais do aluno.
    @Override
    public String toString() {
        return "Aluno{" +
                "identificadorAcademico='" + identificadorAcademico + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
