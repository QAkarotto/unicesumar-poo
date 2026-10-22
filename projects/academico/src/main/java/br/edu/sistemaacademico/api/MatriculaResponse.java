package br.edu.sistemaacademico.api;

public record MatriculaResponse(String id, String identificadorAluno,
                                String nomeAluno, String disciplina, String resultado) {
}