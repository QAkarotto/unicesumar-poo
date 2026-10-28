package br.edu.sistemaacademico.api;

public record NovoAlunoRequest(
        String registroAcademico,
        String nome,
        String email
) {
}
