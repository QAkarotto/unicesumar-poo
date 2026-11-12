package br.edu.aniva.domain;

import br.edu.aniva.Fabrica;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AvaliacaoTest {

    private final Usuario usuario = Fabrica.usuario(1L, "Ana");
    private final Obra obra = Fabrica.obra("frieren");

    @Test
    void deveAceitarNotasDe1a10() {
        assertEquals(1, new Avaliacao(usuario, obra, 1).getNota());
        assertEquals(10, new Avaliacao(usuario, obra, 10).getNota());
    }

    @Test
    void deveRejeitarNotasForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(usuario, obra, 0));
        assertThrows(IllegalArgumentException.class, () -> new Avaliacao(usuario, obra, 11));
    }

    @Test
    void naoDeveAlterarParaNotaInvalida() {
        var avaliacao = new Avaliacao(usuario, obra, 8);

        assertThrows(IllegalArgumentException.class, () -> avaliacao.alterarNota(15));

        assertEquals(8, avaliacao.getNota());
    }
}
