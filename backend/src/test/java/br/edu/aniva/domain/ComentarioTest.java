package br.edu.aniva.domain;

import br.edu.aniva.Fabrica;
import br.edu.aniva.exception.OperacaoNaoPermitidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComentarioTest {

    private final Usuario autor = Fabrica.usuario(1L, "Ana");
    private final Usuario outraPessoa = Fabrica.usuario(2L, "Bia");
    private final Obra obra = Fabrica.obra("frieren");

    @Test
    void soOAutorPodeApagarOComentario() {
        var comentario = new Comentario(obra, autor, "Obra incrível", false);

        assertDoesNotThrow(() -> comentario.exigirAutor(autor));
        assertThrows(OperacaoNaoPermitidaException.class, () -> comentario.exigirAutor(outraPessoa));
    }

    @Test
    void curtirDuasVezesDeveRemoverACurtida() {
        var comentario = new Comentario(obra, autor, "Obra incrível", false);

        comentario.alternarCurtida(outraPessoa);
        assertEquals(1, comentario.idsDeQuemCurtiu().size());
        assertTrue(comentario.idsDeQuemCurtiu().contains(2L));

        comentario.alternarCurtida(outraPessoa);
        assertTrue(comentario.idsDeQuemCurtiu().isEmpty());
    }

    @Test
    void deveRejeitarTextoVazioOuMuitoLongo() {
        assertThrows(IllegalArgumentException.class, () -> new Comentario(obra, autor, "   ", false));
        assertThrows(IllegalArgumentException.class, () -> new Comentario(obra, autor, "a".repeat(1001), false));
    }
}
