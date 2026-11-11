package br.edu.sistemaacademico.dto;

public record CriarMatriculaRequest(
        String registroAcademico,
        String ofertaId,
        String raAluno,
        Long idOferta
) {
    public String obterRegistroAcademico() {
        if (registroAcademico != null && !registroAcademico.isBlank()) {
            return registroAcademico.trim();
        }
        if (raAluno != null && !raAluno.isBlank()) {
            return raAluno.trim();
        }
        return null;
    }

    public String obterOfertaIdentificador() {
        if (ofertaId != null && !ofertaId.isBlank()) {
            return ofertaId.trim();
        }
        if (idOferta != null) {
            return idOferta.toString();
        }
        return null;
    }
}
