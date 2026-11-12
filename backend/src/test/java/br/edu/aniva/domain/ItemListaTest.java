package br.edu.aniva.domain;

import br.edu.aniva.Fabrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemListaTest {

    private ItemLista item;

    @BeforeEach
    void criarItem() {
        item = new ItemLista(Fabrica.usuario(1L, "Ana"), Fabrica.obra("frieren"));
    }

    @Test
    void deveEntrarNaPautaComoAFazerComProgressoZero() {
        item.alternarPauta();

        assertTrue(item.naPauta());
        assertEquals(StatusPauta.A_FAZER, item.getStatus());
        assertEquals(0, item.getProgresso());
    }

    @Test
    void deveSairDaPautaAoAlternarDeNovo() {
        item.atualizarPauta(StatusPauta.ANDAMENTO, 40);

        item.alternarPauta();

        assertFalse(item.naPauta());
        assertEquals(0, item.getProgresso());
    }

    @Test
    void concluirDeveForcarProgressoEm100() {
        item.atualizarPauta(StatusPauta.ANDAMENTO, 30);

        item.atualizarPauta(StatusPauta.CONCLUIDO, null);

        assertEquals(100, item.getProgresso());
    }

    @Test
    void voltarParaAFazerDeveZerarOProgresso() {
        item.atualizarPauta(StatusPauta.ANDAMENTO, 60);

        item.atualizarPauta(StatusPauta.A_FAZER, null);

        assertEquals(0, item.getProgresso());
    }

    @Test
    void deveRejeitarProgressoForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class, () -> item.atualizarPauta(StatusPauta.ANDAMENTO, 101));
        assertThrows(IllegalArgumentException.class, () -> item.atualizarPauta(StatusPauta.ANDAMENTO, -1));
    }

    @Test
    void deveRejeitarAtualizacaoSemNenhumDado() {
        assertThrows(IllegalArgumentException.class, () -> item.atualizarPauta(null, null));
    }

    @Test
    void itemSoFicaVazioSemFavoritoENaPauta() {
        item.alternarFavorito();
        item.alternarPauta();
        assertFalse(item.estaVazio());

        item.alternarFavorito();
        item.alternarPauta();
        assertTrue(item.estaVazio());
    }
}
