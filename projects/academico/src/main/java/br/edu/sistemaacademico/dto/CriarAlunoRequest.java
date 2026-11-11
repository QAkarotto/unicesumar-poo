package br.edu.sistemaacademico.dto;

public record CriarAlunoRequest(
        String registroAcademico,
        String ra,
        String nome,
        String email
) {
    public String obterRa() {
        if (registroAcademico != null && !registroAcademico.isBlank()) {
            return registroAcademico.trim();
        }
        if (ra != null && !ra.isBlank()) {
            return ra.trim();
        }
        return null;
    }
}
