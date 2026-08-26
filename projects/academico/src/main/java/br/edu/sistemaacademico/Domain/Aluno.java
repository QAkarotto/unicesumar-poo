package br.edu.sistemaacademico.domain;

import java.util.regex.Pattern;

public class Aluno {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final String identificador;
    private final String nome;
    private String email;

    public Aluno(String identificador, String nome, String email) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("Identificador acadêmico é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório.");
        }
        validarEmail(email);

        this.identificador = identificador;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String novoEmail) {
        validarEmail(novoEmail);
        this.email = novoEmail;
    }

    private static void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail do aluno é obrigatório.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("E-mail do aluno é inválido: " + email);
        }
    }

    @Override
    public String toString() {
        return nome + " (" + identificador + ") <" + email + ">";
    }
}

private final List<Matricula> historico = new ArrayList<>();

void registrarMatricula(Matricula matricula) {
    historico.add(matricula);
}

public boolean jaFoiAprovadoEm(Disciplina disciplina) {
    return historico.stream()
            .anyMatch(matricula ->
                    matricula.getOfertaDisciplina().getDisciplina().getCodigo().equals(disciplina.getCodigo())
                    && matricula.getResultado() == ResultadoAcademico.APROVADO);
}

public List<Matricula> getHistorico() {
    return Collections.unmodifiableList(historico);
}