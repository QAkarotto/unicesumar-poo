package br.edu.seugame.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import org.junit.jupiter.api.Test;

class RegrasDominioTest {
    private final Clock hoje = Clock.fixed(Instant.parse("2026-11-08T12:00:00Z"), ZoneOffset.UTC);
    private Torneio torneio(TipoTorneio tipo) {
        return new Torneio(new Usuario("Organizador", "organizador", "teste@example.invalid", "hash-teste"),
                "Copa Teste", "", tipo, LocalDate.of(2026,12,1), hoje);
    }
    private Participante participante(Torneio t, String nome, int ordem) {
        Participante p = new Participante(t,nome,ordem);
        try {
            var campo = Participante.class.getDeclaredField("id"); campo.setAccessible(true); campo.set(p,(long)ordem);
        } catch (ReflectiveOperationException ex) { throw new AssertionError(ex); }
        return p;
    }
    private Partida partida(Torneio t) {
        var a = participante(t,"Ana",1); var b = participante(t,"Bruno",2);
        return new Partida(t,a,b,1,1,t.getTipo()==TipoTorneio.TABELA?FasePartida.LIGA:FasePartida.CHAVE);
    }
    private Torneio encerrado() {
        var t=torneio(TipoTorneio.TABELA);
        var a=participante(t,"Ana",1); var b=participante(t,"Bruno",2); var c=participante(t,"Carlos",3);
        new Partida(t,a,b,1,1,FasePartida.LIGA).registrarResultado(2,0);
        new Partida(t,a,c,2,1,FasePartida.LIGA).registrarResultado(2,0);
        new Partida(t,b,c,3,1,FasePartida.LIGA).registrarResultado(1,0);
        t.finalizar(a); return t;
    }
    @Test void deveValidarUsuarioSemAlteracaoParcial() {
        assertThrows(IllegalArgumentException.class,()->new Usuario("Ab","valido","a@example.invalid","hash"));
        var u=new Usuario("Nome Válido","valido","a@example.invalid","hash");
        assertThrows(IllegalArgumentException.class,()->u.alterarPerfil("Outro Nome","invalido"));
        assertEquals("Nome Válido",u.getNome());
    }
    @Test void deveValidarTorneio() {
        assertThrows(IllegalArgumentException.class,()->new Torneio(new Usuario("Nome","nome","a@b.invalid","hash"),"ab","",TipoTorneio.TABELA,LocalDate.now(hoje),hoje));
    }
    @Test void deveNormalizarNomeERejeitarDuplicata() {
        var t=torneio(TipoTorneio.TABELA); var a=participante(t," \tAna\u00a0",1);
        assertEquals("Ana",a.getNome());
        assertThrows(IllegalArgumentException.class,()->participante(t," ANA ",2));
        assertEquals(1,t.getParticipantes().size());
        assertThrows(IllegalArgumentException.class,()->Participante.validarNome("  "));
        assertThrows(IllegalArgumentException.class,()->Participante.validarNome("a".repeat(101)));
    }
    @Test void deveExigirTresParticipantesNaPreparacao() {
        var t=torneio(TipoTorneio.TABELA); participante(t,"Ana",1); participante(t,"Bruno",2);
        assertThrows(IllegalStateException.class,t::validarParticipantes);
        participante(t,"Carlos",3); assertDoesNotThrow(t::validarParticipantes);
        assertThrows(IllegalStateException.class,()->t.removerParticipante(t.getParticipantes().getFirst()));
    }
    @Test void deveBloquearFormatoAoIniciarMasPermitirParticipantesSemPlacar() {
        var t=torneio(TipoTorneio.TABELA); t.iniciar();
        assertFalse(t.podeEditarFormato()); assertTrue(t.podeAlterarParticipantes());
        assertThrows(IllegalStateException.class,()->t.alterarFormato(TipoTorneio.CHAVEAMENTO,hoje));
    }
    @Test void deveConsiderarDataNaPermissaoDeFormato() {
        var t=torneio(TipoTorneio.TABELA);
        assertTrue(t.podeEditarFormato(hoje));
        assertFalse(t.podeEditarFormato(Clock.fixed(Instant.parse("2026-12-01T12:00:00Z"),ZoneOffset.UTC)));
        assertThrows(IllegalStateException.class,()->t.alterarFormato(TipoTorneio.CHAVEAMENTO,hoje));
    }
    @Test void mudancaDeFormatoNaoDeveRecriarPartidasMasExigeReconstrucaoAntesDoPlacar() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t);
        t.alterarFormato(TipoTorneio.CHAVEAMENTO,hoje);
        assertSame(p,t.getPartidas().getFirst());
        assertThrows(IllegalStateException.class,()->p.registrarResultado(0,0));
    }
    @Test void deveBloquearParticipantesAposResultado() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t); p.registrarResultado(0,0);
        assertEquals(StatusTorneio.ANDAMENTO,t.getStatus()); assertFalse(t.podeAlterarParticipantes());
        assertThrows(IllegalStateException.class,()->participante(t,"Carlos",3));
        assertThrows(IllegalStateException.class,()->p.getParticipante1().alterarNome("Outra"));
    }
    @Test void deveRejeitarPlacaresSemMutacao() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t);
        for(int valor:new int[]{-1,1000,Integer.MAX_VALUE}) {
            assertThrows(IllegalArgumentException.class,()->p.registrarResultado(valor,0));
            assertThrows(IllegalArgumentException.class,()->p.registrarResultado(0,valor));
        }
        assertNull(p.getPlacar1()); assertEquals(StatusTorneio.FUTURO,t.getStatus());
        p.registrarResultado(999,0); assertEquals(p.getParticipante1(),p.getVencedor());
        p.registrarResultado(0,1); assertEquals(p.getParticipante2(),p.getVencedor());
    }
    @Test void deveRejeitarEmpateEliminatorio() {
        var p=partida(torneio(TipoTorneio.CHAVEAMENTO));
        assertThrows(IllegalArgumentException.class,()->p.registrarResultado(1,1)); assertFalse(p.temResultado());
    }
    @Test void deveRejeitarParticipanteExternoEVencedorIncorreto() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t);
        var externo=participante(torneio(TipoTorneio.TABELA),"Externo",1);
        assertThrows(IllegalArgumentException.class,()->new Partida(t,externo,p.getParticipante2(),2,1,FasePartida.LIGA));
        assertThrows(IllegalArgumentException.class,()->new Classificacao(t,externo));
        assertThrows(IllegalArgumentException.class,()->new Partida(t,p.getParticipante1(),p.getParticipante2(),2,1,FasePartida.LIGA,(short)2,(short)1,p.getParticipante2()));
    }
    @Test void deveAtualizarDataSemRetrocederEstado() {
        var t=torneio(TipoTorneio.TABELA);
        var dezembro=Clock.fixed(Instant.parse("2026-12-01T12:00:00Z"),ZoneOffset.UTC);
        t.alterarDadosBasicos("Nova Copa","Descrição",LocalDate.of(2030,1,1),dezembro);
        assertEquals(StatusTorneio.ANDAMENTO,t.getStatus());
        t.atualizarEstado(hoje); assertEquals(StatusTorneio.ANDAMENTO,t.getStatus());
    }
    @Test void edicaoDescritivaNaoDeveMexerNaEstrutura() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t); var a=p.getParticipante1();
        t.alterarDadosBasicos("Nome alterado","Outra descrição",LocalDate.of(2027,1,1),hoje);
        assertSame(p,t.getPartidas().getFirst()); assertSame(a,t.getParticipantes().getFirst()); assertEquals(1,a.getOrdemSorteio());
        assertThrows(UnsupportedOperationException.class,()->t.getPartidas().clear());
    }
    @Test void encerramentoIncompletoOuSemCampeaoDeveFalhar() {
        var t=torneio(TipoTorneio.TABELA); var p=partida(t); participante(t,"Carlos",3);
        assertThrows(IllegalStateException.class,()->t.finalizar(p.getParticipante1()));
        assertThrows(IllegalArgumentException.class,()->t.finalizar(null));
        assertEquals(StatusTorneio.FUTURO,t.getStatus());
    }
    @Test void finalizadoDeveRejeitarTodaAlteracao() {
        var t=encerrado(); assertEquals(StatusTorneio.FINALIZADO,t.getStatus());
        assertThrows(IllegalStateException.class,t::iniciar);
        assertThrows(IllegalStateException.class,t::validarExclusao);
        assertThrows(IllegalStateException.class,()->t.alterarDadosBasicos("Outro","",LocalDate.now(hoje),hoje));
        assertThrows(IllegalStateException.class,()->t.getPartidas().getFirst().registrarResultado(0,1));
        t.atualizarEstado(hoje); assertEquals(StatusTorneio.FINALIZADO,t.getStatus());
    }
}
