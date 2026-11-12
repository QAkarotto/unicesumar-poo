package br.edu.seugame.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChaveamentoTest {
    @ParameterizedTest @ValueSource(ints={3,5,6,8})
    void deveGerarChaveCompletaEFinalizar(int n) {
        var t=CompeticaoFixture.criar(TipoTorneio.CHAVEAMENTO,n);
        var jogos=new GeradorChaveamento().gerar(t);
        int tamanho=1;while(tamanho<n)tamanho*=2;
        assertEquals(tamanho-1,jogos.size());
        assertEquals(Integer.numberOfTrailingZeros(tamanho),jogos.stream().mapToInt(Partida::getRodada).max().orElseThrow());
        int byes=0;Set<Long> ids=new HashSet<>();
        for(Partida p:jogos) {
            if(p.getRodada()!=1)continue;
            assertTrue(p.getParticipante1()!=null || p.getParticipante2()!=null);
            if(p.getParticipante1()!=null)assertTrue(ids.add(p.getParticipante1().getId()));
            if(p.getParticipante2()!=null)assertTrue(ids.add(p.getParticipante2().getId()));
            if(p.getParticipante2()==null) {
                byes++;assertSame(p.getParticipante1(),p.getVencedor());assertNull(p.getPlacar1());
            }
        }
        assertEquals(n,ids.size());assertEquals(tamanho-n,byes);
        assertThrows(IllegalStateException.class,t::finalizar);
        for(Partida p:jogos) {
            if(p.getParticipante1()!=null && p.getParticipante2()!=null) {
                assertThrows(IllegalArgumentException.class,()->p.registrarResultado(1,1));
                p.registrarResultado(2,0);
            }
        }
        assertEquals(n-1,jogos.stream().filter(Partida::temResultado).count());
        Partida finalissima=jogos.getLast();
        t.finalizar();assertEquals(finalissima.getVencedor().getId(),t.getCampeao().getId());
        assertThrows(IllegalStateException.class,()->new ReconstrutorChaveamento().reconstruir(t));
        assertThrows(IllegalStateException.class,()->jogos.getFirst().registrarResultado(0,1));
    }

    @Test void devePreservarVagasExatasDoPhpParaCinco() {
        var t=CompeticaoFixture.criar(TipoTorneio.CHAVEAMENTO,5);
        var jogos=new GeradorChaveamento().gerar(t);
        assertEquals(List.of("1x5","2x-","3x-","4x-"),jogos.stream().filter(p->p.getRodada()==1)
                .map(p->p.getParticipante1().getId()+"x"+(p.getParticipante2()==null?"-":p.getParticipante2().getId())).toList());
        assertNull(jogos.get(4).getParticipante1());
        assertEquals(2L,jogos.get(4).getParticipante2().getId());
        assertNull(jogos.get(4).getVencedor()); // uma vaga pendente não é BYE
        assertNull(jogos.getLast().getParticipante1());
        assertThrows(IllegalStateException.class,()->jogos.get(4).registrarResultado(1,0));
    }

    @Test void correcaoComMesmoVencedorDevePreservarResultadosPosteriores() {
        var t=completo();var jogos=t.getPartidas();
        var finalissima=jogos.getLast();Long campeao=finalissima.getVencedor().getId();
        jogos.getFirst().registrarResultado(5,0);
        assertEquals((short)2,jogos.get(4).getPlacar1());
        assertEquals((short)2,finalissima.getPlacar1());assertEquals(campeao,finalissima.getVencedor().getId());
    }

    @Test void trocaDeVencedorDeveLimparSomenteDependentes() {
        var t=completo();var jogos=t.getPartidas();
        var independente=jogos.get(5);var vencedorIndependente=independente.getVencedor();
        jogos.getFirst().registrarResultado(0,3);
        assertEquals(5L,jogos.get(4).getParticipante1().getId());
        assertNull(jogos.get(4).getPlacar1());assertNull(jogos.get(4).getVencedor());
        assertEquals((short)2,independente.getPlacar1());assertSame(vencedorIndependente,independente.getVencedor());
        assertNull(jogos.getLast().getParticipante1());assertNull(jogos.getLast().getPlacar1());
        assertSame(vencedorIndependente,jogos.getLast().getParticipante2());
        assertThrows(IllegalStateException.class,t::finalizar);
        jogos.get(4).registrarResultado(1,0);jogos.getLast().registrarResultado(1,0);
        t.finalizar();assertEquals(5L,t.getCampeao().getId());
    }

    @Test void somenteUmResultadoDaPrimeiraRodadaDeveOcuparOSlotCorreto() {
        var t=CompeticaoFixture.criar(TipoTorneio.CHAVEAMENTO,8);var jogos=new GeradorChaveamento().gerar(t);
        jogos.get(1).registrarResultado(0,1);
        assertNull(jogos.get(4).getParticipante1());assertEquals(6L,jogos.get(4).getParticipante2().getId());
        assertNull(jogos.get(4).getVencedor());
    }

    @Test void reconstruirDeveSerIdempotente() {
        var t=completo();var campeao=t.getPartidas().getLast().getVencedor();
        new ReconstrutorChaveamento().reconstruir(t);new ReconstrutorChaveamento().reconstruir(t);
        assertEquals(7,t.getPartidas().stream().filter(Partida::temResultado).count());
        assertSame(campeao,t.getPartidas().getLast().getVencedor());
    }

    private Torneio completo() {
        var t=CompeticaoFixture.criar(TipoTorneio.CHAVEAMENTO,8);
        for(var p:new GeradorChaveamento().gerar(t))p.registrarResultado(2,0);
        return t;
    }
}
