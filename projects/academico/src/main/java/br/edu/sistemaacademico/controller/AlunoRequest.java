package br.edu.sistemaacademico.controller;

public record AlunoRequest(
        String identificadorAcademico,
        String nome,
        String email
) {
}
