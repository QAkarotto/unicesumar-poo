package br.edu.sistemaacademico.api;

/** Corpo do POST /api/alunos. */
public record AlunoRequest(String registroAcademico, String nome, String email) {
}
