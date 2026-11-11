package br.edu.agroassist.domain;

import br.edu.agroassist.exception.DadosInvalidosException;
import br.edu.agroassist.exception.OperacaoNaoPermitidaException;
import br.edu.agroassist.exception.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoTest {

    private static final LocalDateTime ABERTURA = LocalDateTime.of(2026, 10, 20, 8, 0);
    private Usuario produtor;
    private Usuario outroProdutor;
    private Usuario coordenador;
    private Usuario usuarioTecnico;
    private Usuario outroUsuarioTecnico;
    private Cultura soja;
    private Cultura trigo;
    private Propriedade propriedade;
    private Tecnico tecnico;
    private Solicitacao solicitacao;

    @BeforeEach
    void prepararDominio() {
        produtor = usuario("u1", Perfil.PRODUTOR);
        outroProdutor = usuario("u2", Perfil.PRODUTOR);
        coordenador = usuario("u6", Perfil.ADMIN);
        usuarioTecnico = usuario("u3", Perfil.TECNICO);
        outroUsuarioTecnico = usuario("u4", Perfil.TECNICO);
        soja = new Cultura("c1", "Soja");
        trigo = new Cultura("c3", "Trigo");
        propriedade =
                new Propriedade(
                        "p1", produtor, "Sítio Esperança", "Castro", new BigDecimal("10.5"), Set.of(soja));
        tecnico = new Tecnico("t1", usuarioTecnico, "Engenheiro agrônomo", "CREA-001", Set.of(soja));
        solicitacao = abrir(produtor, soja, "Folhas com manchas.", List.of());
    }

    @Test
    void aberturaRegistraStatusAutorEHorarioNoHistorico() {
        assertEquals(Status.ABERTA, solicitacao.getStatus());
        assertEquals(1, solicitacao.getHistorico().size());
        EventoHistorico evento = solicitacao.getHistorico().getFirst();
        assertEquals(Status.ABERTA, evento.getStatus());
        assertEquals(produtor, evento.getAutor());
        assertEquals(ABERTURA, evento.getEm());
    }

    @Test
    void naoAbreSolicitacaoParaCulturaAusenteNaPropriedade() {
        assertThrows(
                RegraNegocioException.class, () -> abrir(produtor, trigo, "Folhas com manchas.", List.of()));
    }

    @Test
    void naoAbreSolicitacaoNaPropriedadeDeOutroProdutor() {
        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> abrir(outroProdutor, soja, "Folhas com manchas.", List.of()));
    }

    @Test
    void naoAbreSolicitacaoSemDescricao() {
        assertThrows(DadosInvalidosException.class, () -> abrir(produtor, soja, "   ", List.of()));
    }

    @Test
    void naoPermiteMaisDeTresFotos() {
        String foto = "data:image/jpeg;base64,/9j/2Q==";
        assertThrows(
                DadosInvalidosException.class,
                () -> abrir(produtor, soja, "Folhas com manchas.", List.of(foto, foto, foto, foto)));
    }

    @Test
    void naoAtribuiTecnicoInativo() {
        tecnico.alterarSituacao(false);

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.atribuir(tecnico, coordenador, ABERTURA.plusHours(1)));
        assertEquals(Status.ABERTA, solicitacao.getStatus());
        assertEquals(1, solicitacao.getHistorico().size());
    }

    @Test
    void naoAtribuiTecnicoSemHabilitacaoNaCultura() {
        Tecnico semHabilitacao =
                new Tecnico("t2", outroUsuarioTecnico, "Técnico agrícola", "CRT-002", Set.of(trigo));

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.atribuir(semHabilitacao, coordenador, ABERTURA.plusHours(1)));
        assertEquals(Status.ABERTA, solicitacao.getStatus());
    }

    @Test
    void somenteCoordenacaoPodeAtribuirTecnico() {
        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> solicitacao.atribuir(tecnico, produtor, ABERTURA.plusHours(1)));
    }

    @Test
    void permiteReatribuicaoAntesDeIniciarOAtendimento() {
        atribuir();
        Tecnico substituto =
                new Tecnico("t2", outroUsuarioTecnico, "Técnico agrícola", "CRT-002", Set.of(soja));

        solicitacao.atribuir(substituto, coordenador, ABERTURA.plusHours(2));
        solicitacao.iniciar(outroUsuarioTecnico, ABERTURA.plusHours(3));

        assertEquals(Status.EM_ATENDIMENTO, solicitacao.getStatus());
        assertEquals(4, solicitacao.getHistorico().size());
    }

    @Test
    void naoPermiteReatribuicaoDepoisDeIniciarOAtendimento() {
        iniciar();
        Tecnico substituto =
                new Tecnico("t2", outroUsuarioTecnico, "Técnico agrícola", "CRT-002", Set.of(soja));

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.atribuir(substituto, coordenador, ABERTURA.plusHours(3)));
        assertEquals(Status.EM_ATENDIMENTO, solicitacao.getStatus());
        assertEquals(3, solicitacao.getHistorico().size());
    }

    @Test
    void somenteTecnicoResponsavelPodeIniciar() {
        atribuir();

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> solicitacao.iniciar(outroUsuarioTecnico, ABERTURA.plusHours(2)));
        assertEquals(Status.ATRIBUIDA, solicitacao.getStatus());
    }

    @Test
    void naoPermiteIniciarDuasVezes() {
        iniciar();

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.iniciar(usuarioTecnico, ABERTURA.plusHours(3)));
        assertEquals(3, solicitacao.getHistorico().size());
    }

    @Test
    void naoConcluiAtendimentoSemAntesIniciar() {
        atribuir();

        assertThrows(RegraNegocioException.class, () -> concluir(usuarioTecnico));
        assertEquals(Status.ATRIBUIDA, solicitacao.getStatus());
    }

    @Test
    void somenteTecnicoResponsavelPodeConcluir() {
        iniciar();

        assertThrows(OperacaoNaoPermitidaException.class, () -> concluir(outroUsuarioTecnico));
        assertEquals(Status.EM_ATENDIMENTO, solicitacao.getStatus());
    }

    @Test
    void diagnosticoVazioNaoConcluiNemAlteraHistorico() {
        iniciar();

        assertThrows(
                DadosInvalidosException.class,
                () ->
                        solicitacao.concluir(
                                usuarioTecnico,
                                ABERTURA.toLocalDate().plusDays(1),
                                Modalidade.VISITA,
                                "   ",
                                "Acompanhar a lavoura.",
                                ABERTURA.plusDays(1)));
        assertEquals(Status.EM_ATENDIMENTO, solicitacao.getStatus());
        assertEquals(3, solicitacao.getHistorico().size());
    }

    @Test
    void fluxoCompletoRegistraAtendimentoAvaliacaoEHistoricoEmOrdem() {
        iniciar();
        Atendimento atendimento = concluir(usuarioTecnico);
        Avaliacao avaliacao =
                solicitacao.avaliar(produtor, 5, "Atendimento esclarecedor.", ABERTURA.plusDays(2));

        assertNotNull(atendimento);
        assertNotNull(avaliacao);
        assertEquals(Status.AVALIADA, solicitacao.getStatus());
        assertEquals(
                List.of(
                        Status.ABERTA,
                        Status.ATRIBUIDA,
                        Status.EM_ATENDIMENTO,
                        Status.CONCLUIDA,
                        Status.AVALIADA),
                solicitacao.getHistorico().stream().map(EventoHistorico::getStatus).toList());
        assertEquals(produtor, solicitacao.getHistorico().getLast().getAutor());
        assertEquals(ABERTURA.plusDays(2), solicitacao.getHistorico().getLast().getEm());
        assertThrows(UnsupportedOperationException.class, () -> solicitacao.getHistorico().clear());
    }

    @Test
    void naoAvaliaAtendimentoAindaNaoConcluido() {
        iniciar();

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.avaliar(produtor, 5, "", ABERTURA.plusDays(2)));
    }

    @Test
    void somenteProdutorQueAbriuPodeAvaliar() {
        iniciar();
        concluir(usuarioTecnico);

        assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> solicitacao.avaliar(outroProdutor, 5, "", ABERTURA.plusDays(2)));
        assertEquals(Status.CONCLUIDA, solicitacao.getStatus());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void naoAceitaNotaForaDeUmACinco(int nota) {
        iniciar();
        concluir(usuarioTecnico);

        assertThrows(
                DadosInvalidosException.class,
                () -> solicitacao.avaliar(produtor, nota, "", ABERTURA.plusDays(2)));
        assertEquals(Status.CONCLUIDA, solicitacao.getStatus());
    }

    @Test
    void naoPermiteAvaliarDuasVezes() {
        iniciar();
        concluir(usuarioTecnico);
        solicitacao.avaliar(produtor, 5, "", ABERTURA.plusDays(2));

        assertThrows(
                RegraNegocioException.class,
                () -> solicitacao.avaliar(produtor, 4, "Segunda tentativa", ABERTURA.plusDays(3)));
        assertEquals(5, solicitacao.getHistorico().size());
    }

    private Usuario usuario(String id, Perfil perfil) {
        return new Usuario(
                id,
                "Usuário " + id,
                perfil,
                "(42) 99999-0000",
                id + "@exemplo.com",
                "Castro",
                ABERTURA.minusDays(1));
    }

    private Solicitacao abrir(Usuario autor, Cultura cultura, String descricao, List<String> fotos) {
        return new Solicitacao(
                "s1", "AGA-2026-0143", autor, propriedade, cultura, "V4", descricao, fotos, ABERTURA);
    }

    private void atribuir() {
        solicitacao.atribuir(tecnico, coordenador, ABERTURA.plusHours(1));
    }

    private void iniciar() {
        atribuir();
        solicitacao.iniciar(usuarioTecnico, ABERTURA.plusHours(2));
    }

    private Atendimento concluir(Usuario autor) {
        return solicitacao.concluir(
                autor,
                ABERTURA.toLocalDate().plusDays(1),
                Modalidade.VISITA,
                "Manchas foliares identificadas.",
                "Acompanhar a evolução da lavoura.",
                ABERTURA.plusDays(1));
    }
}
