package br.edu.seugame.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TabelaTest {
    @ParameterizedTest @ValueSource(ints = {3,4,5,6})
    void deveGerarRodizioCompletoComFolgasCorretas(int n) {
        Torneio t = CompeticaoFixture.criar(TipoTorneio.TABELA,n);
        var jogos = new GeradorTabela().gerar(t);
        assertEquals(n*(n-1)/2,jogos.size());
        int rodadas = n%2==0?n-1:n;
        assertEquals(rodadas,jogos.stream().mapToInt(Partida::getRodada).max().orElseThrow());
        var pares = new HashSet<String>();
        var presencas = new HashMap<Long,Integer>();
        for (int rodada=1;rodada<=rodadas;rodada++) {
            Set<Long> ocupados = new HashSet<>(); int ordem=1;
            for(Partida p:jogos) {
                if(p.getRodada()!=rodada) continue;
                long a=p.getParticipante1().getId(),b=p.getParticipante2().getId();
                assertNotEquals(a,b); assertEquals(ordem++,p.getOrdem());
                assertTrue(pares.add(Math.min(a,b)+":"+Math.max(a,b)));
                assertTrue(ocupados.add(a)); assertTrue(ocupados.add(b));
                presencas.merge(a,1,Integer::sum); presencas.merge(b,1,Integer::sum);
            }
            assertEquals(n%2==0?n:n-1,ocupados.size());
        }
        assertTrue(presencas.values().stream().allMatch(v->v==n-1));
        assertThrows(IllegalStateException.class,()->new GeradorTabela().gerar(t));
    }

    @Test void devePreservarDistribuicaoExataDoPhpParaQuatroParticipantes() {
        var t=CompeticaoFixture.criar(TipoTorneio.TABELA,4);
        var pares=new GeradorTabela().gerar(t).stream()
                .map(p->p.getParticipante1().getId()+"x"+p.getParticipante2().getId()).toList();
        assertEquals(List.of("1x4","2x3","3x1","4x2","1x2","3x4"),pares);
    }

    @Test void deveContabilizarEstatisticasECorrigirSemSomarDuasVezes() {
        var t=CompeticaoFixture.criar(TipoTorneio.TABELA,3);
        var jogos=new GeradorTabela().gerar(t);
        jogos.get(0).registrarResultado(2,0);
        jogos.get(1).registrarResultado(1,1);
        var calculador=new ClassificadorTabela();
        var linhas=calculador.recalcular(t);
        var segundo=linhas.stream().filter(c->c.getParticipante().getId()==2L).findFirst().orElseThrow();
        assertAll(()->assertEquals(3,segundo.getPontos()),()->assertEquals(1,segundo.getJogos()),
                ()->assertEquals(2,segundo.getGolsPro()),()->assertEquals(0,segundo.getGolsContra()),
                ()->assertEquals(2,segundo.getSaldo()),()->assertEquals(1,segundo.getVitorias()));
        jogos.get(0).registrarResultado(0,1);
        var corrigidas=calculador.recalcular(t,linhas);
        assertTrue(corrigidas.contains(segundo));
        assertEquals(0,segundo.getPontos()); assertEquals(1,segundo.getJogos()); assertEquals(1,segundo.getDerrotas());
        assertEquals(-1,segundo.getSaldo());
        var terceiro=corrigidas.getFirst();
        assertEquals(3L,terceiro.getParticipante().getId()); assertEquals(4,terceiro.getPontos());
        assertEquals(2,terceiro.getJogos()); assertEquals(1,terceiro.getEmpates());
        assertThrows(IllegalStateException.class,t::finalizar);
    }

    @ParameterizedTest @ValueSource(ints={0,1,2,3,4})
    void deveAplicarTodosOsCriteriosDeDesempate(int criterio) {
        var t=CompeticaoFixture.criar(TipoTorneio.TABELA,3);
        var a=new Classificacao(t,t.getParticipantes().get(0));
        var b=new Classificacao(t,t.getParticipantes().get(1));
        if(criterio<4) {a.getParticipante().alterarNome("Zulu");b.getParticipante().alterarNome("Ana");}
        switch(criterio) {
            case 0 -> {for(int i=0;i<4;i++)a.contabilizar(0,0);b.contabilizar(9,0);}
            case 1 -> {a.contabilizar(1,0);a.contabilizar(0,5);for(int i=0;i<3;i++)b.contabilizar(1,1);}
            case 2 -> {a.contabilizar(3,0);b.contabilizar(10,9);}
            case 3 -> {a.contabilizar(3,2);b.contabilizar(1,0);}
            case 4 -> {a.contabilizar(1,0);b.contabilizar(1,0);}
        }
        assertSame(a,new ClassificadorTabela().ordenar(List.of(b,a)).getFirst());
    }

    @Test void deveEscolherCampeaoPeloMesmoDesempateEBloquearAposFinalizacao() {
        var t=CompeticaoFixture.criar(TipoTorneio.TABELA,3);
        t.getParticipantes().get(2).alterarNome("Ana");
        for(var p:new GeradorTabela().gerar(t))p.registrarResultado(0,0);
        var primeiro=new ClassificadorTabela().recalcular(t).getFirst().getParticipante();
        assertEquals(3L,primeiro.getId());
        assertThrows(IllegalArgumentException.class,()->t.finalizar(t.getParticipantes().getFirst()));
        t.finalizar(); assertSame(primeiro,t.getCampeao());
        assertThrows(IllegalStateException.class,()->t.getPartidas().getFirst().registrarResultado(1,0));
        assertThrows(IllegalStateException.class,()->new ClassificadorTabela().recalcular(t));
    }
}
