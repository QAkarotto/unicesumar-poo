package br.edu.sistemaacademico.api;

/**
 * JSON recebido no POST /api/matriculas. Exemplo:
 * { "registroAcademico": "RA2026003", "turma": "ESOFT4S-NB", "disciplina": "BD" }
 *
 * O Spring (Jackson) lê o corpo da requisição e monta este record automaticamente.
 */
public record NovaMatriculaRequest(
        String registroAcademico,
        String turma,
        String disciplina
) {
    /** Só confere se os três campos vieram preenchidos; as regras de matrícula ficam no domínio. */
    public boolean estaCompleto() {
        return preenchido(registroAcademico) && preenchido(turma) && preenchido(disciplina);
    }

    private static boolean preenchido(String valor) {
        return valor != null && !valor.isBlank();
    }
}
