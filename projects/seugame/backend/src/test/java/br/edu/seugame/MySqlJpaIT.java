package br.edu.seugame;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.seugame.domain.*;
import br.edu.seugame.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

class MySqlJpaIT {
    private static ConfigurableApplicationContext context;
    private static EntityManager em;
    private static JdbcTemplate jdbc;
    private static TransactionTemplate transactions;
    private static UsuarioRepository usuarios;
    private static TorneioRepository torneios;
    private static ParticipanteRepository participantes;
    private static PartidaRepository partidas;
    private static ClassificacaoRepository classificacoes;
    private static Map<String, String> schemaInicial;

    @BeforeAll
    static void iniciarComSchemaExistente() {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv().getOrDefault("TEST_DB_PASSWORD", "");
        assertNotNull(url, "Informe TEST_DB_URL ou use scripts/test-mysql.ps1.");
        assertTrue(url.startsWith("jdbc:mysql://"), "O teste exige MySQL real.");
        assertNotNull(user, "Informe TEST_DB_USER.");
        var ddlDataSource = new DriverManagerDataSource(url, user, password);
        var ddl = new JdbcTemplate(ddlDataSource);
        String database = ddl.queryForObject("SELECT DATABASE()", String.class);
        assertNotNull(database);
        assertTrue(database.matches("seugame_jpa_it_[a-zA-Z0-9_]+"),
                "Use um banco descartável novo com prefixo seugame_jpa_it_.");
        assertEquals(0, ddl.queryForObject(
                "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE()", Integer.class),
                "O banco deve estar vazio; o teste não apaga nem reutiliza tabelas existentes.");
        new ResourceDatabasePopulator(new FileSystemResource(
                Path.of(System.getProperty("seugame.schema", "../database/schema.sql"))))
                .execute(ddlDataSource);
        schemaInicial = snapshot(ddl);

        context = new SpringApplicationBuilder(SeuGameApplication.class).web(WebApplicationType.NONE)
                .run("--spring.profiles.active=mysql-it", "--spring.config.import=",
                        "--spring.datasource.url=" + url, "--spring.datasource.username=" + user,
                        "--spring.datasource.password=" + password);
        var factory = context.getBean(EntityManagerFactory.class);
        assertEquals("validate", factory.getProperties().get("hibernate.hbm2ddl.auto"));
        em = SharedEntityManagerCreator.createSharedEntityManager(factory);
        jdbc = new JdbcTemplate(context.getBean(DataSource.class));
        transactions = new TransactionTemplate(context.getBean(PlatformTransactionManager.class));
        usuarios = context.getBean(UsuarioRepository.class);
        torneios = context.getBean(TorneioRepository.class);
        participantes = context.getBean(ParticipanteRepository.class);
        partidas = context.getBean(PartidaRepository.class);
        classificacoes = context.getBean(ClassificacaoRepository.class);
        assertEquals(schemaInicial, snapshot(jdbc), "A inicialização não deve alterar o schema SQL.");
    }

    @AfterAll
    static void encerrar() {
        if (context != null) {
            try {
                assertEquals(schemaInicial, snapshot(jdbc), "Os testes não devem modificar o DDL.");
            } finally {
                context.close();
            }
        }
    }

    @Test
    void devePersistirUsuarioTorneioEnumsEDataGeradaPeloMySql() {
        inTransaction(entityManager -> {
            LocalDateTime antes = LocalDateTime.now(ZoneOffset.UTC).minusSeconds(2);
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            assertNotNull(base.torneio().getCriadoEm());
            LocalDateTime noBanco = jdbc.queryForObject("SELECT criado_em FROM torneios WHERE id=?",
                    (rs, row) -> rs.getObject(1, LocalDateTime.class), base.torneio().getId());
            assertEquals(noBanco, base.torneio().getCriadoEm());
            assertFalse(base.torneio().getCriadoEm().isBefore(antes), "A data deve permanecer em UTC.");
            entityManager.clear();
            Usuario usuario = usuarios.findByUsuario("ORGANIZADOR").orElseThrow();
            assertEquals("Pessoa Organizadora", usuario.getNome());
            assertEquals("hash-ficticio-de-teste", usuario.getSenhaHash());
            assertEquals(usuario.getId(), usuarios.findByEmail("TESTE@example.invalid").orElseThrow().getId());
            Torneio torneio = torneios.findById(base.torneio().getId()).orElseThrow();
            assertEquals(usuario.getId(), torneio.getCriador().getId());
            assertEquals(TipoTorneio.TABELA, torneio.getTipo());
            assertEquals(StatusTorneio.ANDAMENTO, torneio.getStatus());
            assertEquals(LocalDate.of(2026, 11, 8), torneio.getDataInicio());
            assertEquals(noBanco, torneio.getCriadoEm());
            assertNull(torneio.getCampeao());
            assertEquals(List.of(torneio.getId()), torneios.findByCriadorIdOrderByCriadoEmDesc(usuario.getId())
                    .stream().map(Torneio::getId).toList());
        });
    }

    @Test
    void deveConsultarParticipantesNaOrdemPersistida() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.FUTURO);
            entityManager.clear();
            List<Participante> lista = participantes.findByTorneioIdOrderByOrdemSorteioAscIdAsc(base.torneio().getId());
            assertEquals(List.of("Ana", "Bruno", "Carlos"), lista.stream().map(Participante::getNome).toList());
            assertEquals(base.torneio().getId(), lista.getFirst().getTorneio().getId());
        });
    }

    @Test
    void devePersistirEConsultarPartidaComJoinComposto() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            Partida partida = partidas.saveAndFlush(new Partida(base.torneio(), base.ana(), base.bruno(), 1, 1, FasePartida.LIGA));
            entityManager.clear();
            Partida lida = partidas.findByIdAndTorneioId(partida.getId(), base.torneio().getId()).orElseThrow();
            assertEquals(base.ana().getId(), lida.getParticipante1().getId());
            assertEquals("Bruno", lida.getParticipante2().getNome());
            assertEquals(base.torneio().getId(), lida.getParticipante1().getTorneio().getId());
            assertEquals(base.usuario().getId(), lida.getTorneio().getCriador().getId());
            assertNull(lida.getVencedor());
            assertNull(lida.getPlacar1());
            assertEquals(FasePartida.LIGA, lida.getFase());
            assertTrue(partidas.findByIdAndTorneioId(partida.getId(), -1L).isEmpty());
            assertEquals(1, partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(base.torneio().getId()).size());
        });
    }

    @Test
    void devePersistirPlacarSmallintEVencedorSemCalcularResultado() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.CHAVEAMENTO, StatusTorneio.FUTURO);
            Partida partida = partidas.saveAndFlush(new Partida(base.torneio(), base.ana(), base.bruno(),
                    1, 1, FasePartida.CHAVE, (short) 999, (short) 0, base.ana()));
            entityManager.clear();
            Partida lida = partidas.findById(partida.getId()).orElseThrow();
            assertEquals((short) 999, lida.getPlacar1());
            assertEquals((short) 0, lida.getPlacar2());
            assertEquals(base.ana().getId(), lida.getVencedor().getId());
            assertEquals("CHAVE", jdbc.queryForObject("SELECT fase FROM partidas WHERE id=?", String.class, lida.getId()));
            assertEquals("CHAVEAMENTO", jdbc.queryForObject("SELECT tipo FROM torneios WHERE id=?", String.class, base.torneio().getId()));
            assertEquals("ANDAMENTO", jdbc.queryForObject("SELECT status FROM torneios WHERE id=?", String.class, base.torneio().getId()));
        });
    }

    @Test
    void deveCarregarByesEPosicoesAindaIndefinidas() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.CHAVEAMENTO, StatusTorneio.FUTURO);
            partidas.saveAndFlush(new Partida(base.torneio(), base.ana(), null, 1, 1,
                    FasePartida.CHAVE, null, null, base.ana()));
            partidas.saveAndFlush(new Partida(base.torneio(), null, base.bruno(), 1, 2,
                    FasePartida.CHAVE, null, null, base.bruno()));
            partidas.saveAndFlush(new Partida(base.torneio(), null, null, 2, 1, FasePartida.CHAVE));
            entityManager.clear();
            List<Partida> lista = partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(base.torneio().getId());
            assertNull(lista.get(0).getParticipante2());
            assertEquals(base.ana().getId(), lista.get(0).getVencedor().getId());
            assertNull(lista.get(1).getParticipante1());
            assertEquals(base.bruno().getId(), lista.get(1).getVencedor().getId());
            assertNull(lista.get(2).getParticipante1());
            assertNull(lista.get(2).getParticipante2());
            assertNull(lista.get(2).getVencedor());
        });
    }

    @Test
    void devePersistirClassificacaoZeroEConsultarParticipante() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            Classificacao inicial = classificacoes.saveAndFlush(new Classificacao(base.torneio(), base.ana()));
            entityManager.clear();
            Classificacao lida = classificacoes.findByTorneioIdAndParticipanteId(
                    base.torneio().getId(), base.ana().getId()).orElseThrow();
            assertEquals(inicial.getId(), lida.getId());
            assertEquals("Ana", lida.getParticipante().getNome());
            assertEquals(base.torneio().getId(), lida.getTorneio().getId());
            assertEquals(0, lida.getJogos());
            assertEquals(0, lida.getPontos());
            assertEquals(1, classificacoes.findByTorneioId(base.torneio().getId()).size());
        });
    }

    @Test
    void deveLerTodasAsEstatisticasPersistidasSemRecalcular() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            Classificacao linha = classificacoes.saveAndFlush(new Classificacao(base.torneio(), base.ana()));
            jdbc.update("UPDATE classificacao SET jogos=3,vitorias=1,empates=1,derrotas=1,gols_pro=2,gols_contra=4,saldo=-2,pontos=4 WHERE id=?", linha.getId());
            entityManager.clear();
            Classificacao lida = classificacoes.findById(linha.getId()).orElseThrow();
            assertAll(() -> assertEquals(3, lida.getJogos()), () -> assertEquals(1, lida.getVitorias()),
                    () -> assertEquals(1, lida.getEmpates()), () -> assertEquals(1, lida.getDerrotas()),
                    () -> assertEquals(2, lida.getGolsPro()), () -> assertEquals(4, lida.getGolsContra()),
                    () -> assertEquals(-2, lida.getSaldo()), () -> assertEquals(4, lida.getPontos()));
        });
    }

    @Test
    void deveLerCampeaoPeloJoinCompostoEStatusFinalizado() {
        inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            jdbc.update("UPDATE torneios SET status='FINALIZADO',campeao_id=? WHERE id=?", base.ana().getId(), base.torneio().getId());
            entityManager.clear();
            Torneio lido = torneios.findById(base.torneio().getId()).orElseThrow();
            assertEquals(StatusTorneio.FINALIZADO, lido.getStatus());
            assertEquals("Ana", lido.getCampeao().getNome());
            assertEquals(lido.getId(), lido.getCampeao().getTorneio().getId());
        });
    }

    @Test
    void deveRejeitarParticipante1DeOutroTorneioPelaFk() {
        assertSqlError(1452, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            Participante externo = externo(base.usuario());
            jdbc.update("INSERT INTO partidas(torneio_id,participante1_id,participante2_id,rodada,ordem,fase) VALUES (?,?,?,1,1,'LIGA')", base.torneio().getId(), externo.getId(), base.bruno().getId());
        }));
    }

    @Test
    void deveRejeitarParticipante2DeOutroTorneioPelaFk() {
        assertSqlError(1452, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            jdbc.update("INSERT INTO partidas(torneio_id,participante1_id,participante2_id,rodada,ordem,fase) VALUES (?,?,?,1,1,'LIGA')", base.torneio().getId(), base.ana().getId(), externo(base.usuario()).getId());
        }));
    }

    @Test
    void deveRejeitarClassificacaoEmOutroTorneioPelaFk() {
        assertSqlError(1452, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            jdbc.update("INSERT INTO classificacao(torneio_id,participante_id) VALUES (?,?)", base.torneio().getId(), externo(base.usuario()).getId());
        }));
    }

    @Test
    void deveRejeitarCampeaoDeOutroTorneioNoBanco() {
        assertSqlError(1452, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            jdbc.update("UPDATE torneios SET status='FINALIZADO',campeao_id=? WHERE id=?", externo(base.usuario()).getId(), base.torneio().getId());
        }));
    }

    @Test
    void deveRejeitarVencedorExternoAoConfrontoNoBanco() {
        assertSqlError(3819, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.CHAVEAMENTO, StatusTorneio.ANDAMENTO);
            jdbc.update("INSERT INTO partidas(torneio_id,participante1_id,participante2_id,vencedor_id,rodada,ordem,fase,placar1,placar2) VALUES (?,?,?,?,1,1,'CHAVE',2,1)", base.torneio().getId(), base.ana().getId(), base.bruno().getId(), externo(base.usuario()).getId());
        }));
    }

    @Test
    void deveRejeitarClassificacaoDuplicada() {
        assertSqlError(1062, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            classificacoes.saveAndFlush(new Classificacao(base.torneio(), base.ana()));
            classificacoes.saveAndFlush(new Classificacao(base.torneio(), base.ana()));
        }));
    }

    @Test
    void deveRespeitarUnicidadeDoNomeSemDiferenciarCaixa() {
        assertSqlError(1062, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            jdbc.update("INSERT INTO participantes(torneio_id,nome,ordem_sorteio) VALUES (?,'ANA',4)", base.torneio().getId());
        }));
    }

    @Test
    void naoDeveExcluirParticipantesEmCascataAoRemoverTorneio() {
        assertSqlError(1451, () -> inTransaction(entityManager -> {
            Base base = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            entityManager.clear();
            torneios.deleteById(base.torneio().getId());
            torneios.flush();
        }));
    }

    @Test
    void deveManterSchemaEColunasVarcharDosEnums() {
        assertEquals(schemaInicial, snapshot(jdbc));
        assertEquals(5, jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE()", Integer.class));
        assertEquals(3, jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND DATA_TYPE='varchar' AND ((TABLE_NAME='torneios' AND COLUMN_NAME IN ('tipo','status')) OR (TABLE_NAME='partidas' AND COLUMN_NAME='fase'))", Integer.class));
        assertEquals(9, jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA=DATABASE() AND CONSTRAINT_TYPE='FOREIGN KEY'", Integer.class));
        assertEquals(20, jdbc.queryForObject("SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA=DATABASE() AND CONSTRAINT_TYPE='CHECK' AND ENFORCED='YES'", Integer.class));
    }

    @Test
    void devePersistirResultadoEPermissoesAposRecarregar() {
        inTransaction(entityManager -> {
            Base b = base(TipoTorneio.TABELA, StatusTorneio.FUTURO);
            Partida p = partidas.saveAndFlush(new Partida(b.torneio(),b.ana(),b.bruno(),1,1,FasePartida.LIGA));
            entityManager.clear();
            Partida carregada = partidas.findById(p.getId()).orElseThrow();
            carregada.registrarResultado(3,1);
            partidas.flush(); entityManager.clear();
            Torneio t = torneios.findById(b.torneio().getId()).orElseThrow();
            assertEquals(StatusTorneio.ANDAMENTO,t.getStatus());
            assertFalse(t.podeAlterarParticipantes()); assertFalse(t.podeEditarFormato());
            assertEquals(b.ana().getId(),partidas.findById(p.getId()).orElseThrow().getVencedor().getId());
        });
    }

    @Test
    void devePersistirFinalizacaoComCampeaoSincronizado() {
        inTransaction(entityManager -> {
            Base b = base(TipoTorneio.TABELA, StatusTorneio.ANDAMENTO);
            Participante c = participantes.findByTorneioIdOrderByOrdemSorteioAscIdAsc(b.torneio().getId()).get(2);
            var p1=partidas.saveAndFlush(new Partida(b.torneio(),b.ana(),b.bruno(),1,1,FasePartida.LIGA)); p1.registrarResultado(2,0);
            var p2=partidas.saveAndFlush(new Partida(b.torneio(),b.ana(),c,2,1,FasePartida.LIGA)); p2.registrarResultado(2,0);
            var p3=partidas.saveAndFlush(new Partida(b.torneio(),b.bruno(),c,3,1,FasePartida.LIGA)); p3.registrarResultado(1,0);
            b.torneio().finalizar(b.ana()); torneios.flush(); entityManager.clear();
            Torneio t=torneios.findById(b.torneio().getId()).orElseThrow();
            assertEquals(StatusTorneio.FINALIZADO,t.getStatus());
            assertEquals(b.ana().getId(),t.getCampeao().getId());
            assertThrows(IllegalStateException.class,t::validarExclusao);
        });
    }

    @Test
    void devePreservarEstruturaNaEdicaoDescritivaPersistida() {
        inTransaction(entityManager -> {
            Base b=base(TipoTorneio.TABELA,StatusTorneio.FUTURO);
            Partida p=partidas.saveAndFlush(new Partida(b.torneio(),b.ana(),b.bruno(),1,1,FasePartida.LIGA));
            b.torneio().alterarDadosBasicos("Novo Nome","Descrição nova",LocalDate.of(2027,1,1),
                    java.time.Clock.fixed(java.time.Instant.parse("2026-11-07T12:00:00Z"),ZoneOffset.UTC));
            torneios.flush(); entityManager.clear();
            assertEquals(p.getId(),partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(b.torneio().getId()).getFirst().getId());
            assertEquals(b.ana().getId(),participantes.findByTorneioIdOrderByOrdemSorteioAscIdAsc(b.torneio().getId()).getFirst().getId());
        });
    }

    @Test
    void deveGerarTabelaRecalcularCorrigirEFinalizarComJpa() {
        inTransaction(entityManager -> {
            Base b=base(TipoTorneio.TABELA,StatusTorneio.FUTURO);
            var jogos=new GeradorTabela().gerar(b.torneio());
            partidas.saveAllAndFlush(jogos);
            for(var jogo:jogos)jogo.registrarResultado(0,0);
            var calculador=new ClassificadorTabela();
            var linhas=calculador.recalcular(b.torneio());
            classificacoes.saveAllAndFlush(linhas);
            var ids=linhas.stream().map(Classificacao::getId).sorted().toList();
            var vencedor=jogos.getFirst().getParticipante1();
            jogos.getFirst().registrarResultado(2,0);
            var corrigidas=calculador.recalcular(b.torneio(),linhas);
            classificacoes.saveAllAndFlush(corrigidas);
            assertEquals(ids,corrigidas.stream().map(Classificacao::getId).sorted().toList());
            assertEquals(vencedor.getId(),corrigidas.getFirst().getParticipante().getId());
            assertEquals(6,corrigidas.stream().mapToInt(Classificacao::getJogos).sum());
            b.torneio().finalizar(); torneios.flush(); entityManager.clear();
            var lido=torneios.findById(b.torneio().getId()).orElseThrow();
            assertEquals(vencedor.getId(),lido.getCampeao().getId());
            assertEquals(3,classificacoes.findByTorneioId(lido.getId()).size());
        });
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(ints={3,5,6,8})
    void devePersistirChaveByesPropagacaoECorrecaoSemRecriarPartidas(int quantidade) {
        inTransaction(entityManager -> {
            Base b=base(TipoTorneio.CHAVEAMENTO,StatusTorneio.FUTURO);
            for(int i=4;i<=quantidade;i++)participantes.saveAndFlush(new Participante(b.torneio(),"Participante "+i,i));
            var geradas=new GeradorChaveamento().gerar(b.torneio());
            partidas.saveAllAndFlush(geradas);
            var ids=geradas.stream().map(Partida::getId).sorted().toList();
            for(var jogo:geradas)if(jogo.getParticipante1()!=null && jogo.getParticipante2()!=null)jogo.registrarResultado(2,0);
            partidas.flush();entityManager.clear();
            Torneio t=torneios.findById(b.torneio().getId()).orElseThrow();
            var jogos=partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(t.getId());
            assertTrue(jogos.getLast().temResultado());
            var primeiro=jogos.getFirst();
            primeiro.registrarResultado(4,0); // preserva os confrontos posteriores
            assertTrue(jogos.getLast().temResultado());
            Long novoClassificado=primeiro.getParticipante2().getId();
            primeiro.registrarResultado(0,3);
            assertNull(jogos.getLast().getPlacar1());
            if(quantidade>=5)assertTrue(jogos.get(jogos.size()-2).temResultado());
            partidas.flush();entityManager.clear();
            Torneio corrigido=torneios.findById(t.getId()).orElseThrow();
            var relidas=partidas.findByTorneioIdOrderByRodadaAscOrdemAsc(t.getId());
            assertEquals(ids,relidas.stream().map(Partida::getId).sorted().toList());
            assertEquals(novoClassificado,relidas.stream().filter(p->p.getRodada()==2 && p.getOrdem()==1).findFirst().orElseThrow().getParticipante1().getId());
            assertThrows(IllegalStateException.class,corrigido::finalizar);
            for(var jogo:relidas)if(!jogo.temResultado() && jogo.getParticipante1()!=null && jogo.getParticipante2()!=null)jogo.registrarResultado(1,0);
            corrigido.finalizar();torneios.flush();entityManager.clear();
            Torneio finalizado=torneios.findById(t.getId()).orElseThrow();
            assertEquals(StatusTorneio.FINALIZADO,finalizado.getStatus());
            assertEquals(novoClassificado,finalizado.getCampeao().getId());
        });
    }

    private static Base base(TipoTorneio tipo, StatusTorneio status) {
        Usuario usuario = usuarios.saveAndFlush(new Usuario("Pessoa Organizadora", "organizador", "teste@example.invalid", "hash-ficticio-de-teste"));
        Torneio torneio = torneios.saveAndFlush(new Torneio(usuario, "Copa de teste", "Descrição", tipo, LocalDate.of(2026, 11, 8), java.time.Clock.fixed(java.time.Instant.parse(status == StatusTorneio.FUTURO ? "2026-11-07T12:00:00Z" : "2026-11-08T12:00:00Z"), ZoneOffset.UTC)));
        Participante bruno = participantes.saveAndFlush(new Participante(torneio, "Bruno", 2));
        Participante ana = participantes.saveAndFlush(new Participante(torneio, "Ana", 1));
        participantes.saveAndFlush(new Participante(torneio, "Carlos", 3));
        return new Base(usuario, torneio, ana, bruno);
    }

    private static Participante externo(Usuario usuario) {
        Torneio outro = torneios.saveAndFlush(new Torneio(usuario, "Outra Copa", "", TipoTorneio.TABELA, LocalDate.of(2026, 12, 1), java.time.Clock.fixed(java.time.Instant.parse("2026-11-08T12:00:00Z"), ZoneOffset.UTC)));
        return participantes.saveAndFlush(new Participante(outro, "Externo", 1));
    }

    private static void inTransaction(Consumer<EntityManager> action) {
        transactions.executeWithoutResult(status -> {
            status.setRollbackOnly();
            action.accept(em);
        });
    }

    private static void assertSqlError(int expectedCode, Runnable action) {
        var failure = assertThrows(org.springframework.dao.DataAccessException.class, action::run);
        Throwable cause = failure;
        while (cause != null && !(cause instanceof SQLException)) {
            cause = cause.getCause();
        }
        assertInstanceOf(SQLException.class, cause);
        assertEquals(expectedCode, ((SQLException) cause).getErrorCode());
    }

    private static Map<String, String> snapshot(JdbcTemplate template) {
        Map<String, String> result = new LinkedHashMap<>();
        for (String table : List.of("usuarios", "torneios", "participantes", "partidas", "classificacao")) {
            String ddl = template.queryForMap("SHOW CREATE TABLE " + table).get("Create Table").toString();
            result.put(table, ddl.replaceAll("AUTO_INCREMENT=\\d+\\s*", ""));
        }
        return result;
    }

    private record Base(Usuario usuario, Torneio torneio, Participante ana, Participante bruno) {}
}
