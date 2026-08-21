package br.edu.sistemaacademico.domain;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    // O RA identifica o aluno, por isso não muda depois que o objeto é criado.
    private final String identificadorAcademico;
    private String nome;
    private String email;

    // O histórico é do aluno: são todas as matrículas que ele já fez, em
    // qualquer turma ou período letivo.
    private final List<Matricula> matriculas = new ArrayList<>();

    public Aluno(String identificadorAcademico, String nome, String email) {
        this.identificadorAcademico = validarIdentificador(identificadorAcademico);
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
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

    // Cópia: o histórico só cresce por dentro do domínio.
    public List<Matricula> getMatriculas() {
        return List.copyOf(matriculas);
    }

    // Sem public: só a oferta chama, logo depois de criar a matrícula.
    void registrarMatricula(Matricula matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula é obrigatória para atualizar o histórico.");
        }
        matriculas.add(matricula);
    }

    // A aprovação vale para a disciplina e não para a turma, então quem
    // responde isso é o histórico do aluno.
    public boolean possuiAprovacaoEm(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("A disciplina é obrigatória para consultar o histórico.");
        }

        for (var matricula : matriculas) {
            if (matricula.aprovouEm(disciplina)) {
                return true;
            }
        }
        return false;
    }

    // A validação acontece antes da atribuição: se o valor for inválido o
    // atributo continua com o conteúdo antigo.
    public void setNome(String nome) {
        this.nome = validarNome(nome);
    }

    public void setEmail(String email) {
        this.email = validarEmail(email);
    }

    private static String validarIdentificador(String identificadorAcademico) {
        if (identificadorAcademico == null || identificadorAcademico.isBlank()) {
            throw new IllegalArgumentException("O identificador acadêmico é obrigatório.");
        }
        return identificadorAcademico.strip();
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        return nome.strip();
    }

    private static String validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail do aluno é obrigatório.");
        }

        var valor = email.strip();
        var posicaoArroba = valor.indexOf('@');
        var posicaoPonto = valor.lastIndexOf('.');

        // Precisa ter algo antes do @, um ponto depois dele e um domínio no final.
        var formatoValido = posicaoArroba > 0
                && posicaoPonto > posicaoArroba + 1
                && posicaoPonto < valor.length() - 1
                && valor.indexOf('@', posicaoArroba + 1) == -1
                && !valor.contains(" ");

        if (!formatoValido) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        return valor;
    }

    // Mesmo RA é o mesmo aluno, então a comparação usa o RA e não a referência.
    @Override
    public boolean equals(Object objeto) {
        return objeto instanceof Aluno outro
                && identificadorAcademico.equals(outro.identificadorAcademico);
    }

    @Override
    public int hashCode() {
        return identificadorAcademico.hashCode();
    }

    @Override
    public String toString() {
        return identificadorAcademico + " - " + nome + " <" + email + ">";
    }
}
