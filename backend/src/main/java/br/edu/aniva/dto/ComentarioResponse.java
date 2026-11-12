package br.edu.aniva.dto;

import br.edu.aniva.domain.Comentario;

import java.time.LocalDateTime;
import java.util.List;

// Nomes dos campos no formato esperado pelo frontend.
public record ComentarioResponse(
        Long id,
        Long authorId,
        String author,
        String text,
        boolean spoiler,
        List<Long> likes,
        LocalDateTime createdAt) {

    public static ComentarioResponse de(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getAutor().getId(),
                comentario.getAutor().getNome(),
                comentario.getTexto(),
                comentario.isSpoiler(),
                comentario.idsDeQuemCurtiu(),
                comentario.getCriadoEm());
    }
}
