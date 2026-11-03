package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.Aluno;
import br.edu.sistemaacademico.Disciplina;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa os Repositories JDBC com um arquivo SQLite temporário
 * (o banco real em database/sistema-academico.db não é tocado).
 */
class RepositoriosJdbcTest {

    @TempDir
    Path pastaTemporaria;

    private String url;
    private ConexaoBanco conexao;

    @BeforeEach
    void prepararBanco() {
        url = "jdbc:sqlite:" + pastaTemporaria.resolve("teste/academico.db");
        conexao = new ConexaoBanco(url);
        new InicializadorBanco(conexao).inicializarSeNecessario();
    }

    @Test
    void criaPastaDoBancoECarregaDadosIniciais() {
        assertTrue(Files.isDirectory(pastaTemporaria.resolve("teste")));

        var aluno = new AlunoRepositoryJdbc(conexao).buscarPorRa("RA2026001");
        assertTrue(aluno.isPresent());
        assertEquals("Aluno Exemplo Um", aluno.get().getNome());

        var disciplina = new DisciplinaRepositoryJdbc(conexao).buscarPorCodigo("POO");
        assertTrue(disciplina.isPresent());
        assertEquals(80, disciplina.get().getCargaHoraria());
    }

    @Test
    void naoRecriaTabelasQuandoBancoJaExiste() {
        new AlunoRepositoryJdbc(conexao)
                .salvar(new Aluno("RA2026100", "Maria Souza", "maria@email.com"));

        boolean recriou = new InicializadorBanco(conexao).inicializarSeNecessario();

        assertFalse(recriou);
        assertTrue(new AlunoRepositoryJdbc(conexao).buscarPorRa("RA2026100").isPresent());
    }

    @Test
    void salvaEConsultaAluno() {
        var repositorio = new AlunoRepositoryJdbc(conexao);

        repositorio.salvar(new Aluno("RA2026100", "Maria Souza", "maria@email.com"));
        var encontrado = repositorio.buscarPorRa("RA2026100");

        assertTrue(encontrado.isPresent());
        assertEquals("RA2026100", encontrado.get().getIdentificadorAcademico());
        assertEquals("Maria Souza", encontrado.get().getNome());
        assertEquals("maria@email.com", encontrado.get().getEmail());
    }

    @Test
    void alunoInexistenteRetornaVazio() {
        assertTrue(new AlunoRepositoryJdbc(conexao).buscarPorRa("RA0000000").isEmpty());
    }

    @Test
    void alunoDuplicadoLancaExcecao() {
        var repositorio = new AlunoRepositoryJdbc(conexao);
        repositorio.salvar(new Aluno("RA2026100", "Maria Souza", "maria@email.com"));

        assertThrows(RegistroDuplicadoException.class, () ->
                repositorio.salvar(new Aluno("RA2026100", "Outra Pessoa", "outra@email.com")));
    }

    @Test
    void salvaEConsultaDisciplina() {
        var repositorio = new DisciplinaRepositoryJdbc(conexao);

        repositorio.salvar(new Disciplina("ED", "Estrutura de Dados", 60));
        var encontrada = repositorio.buscarPorCodigo("ED");

        assertTrue(encontrada.isPresent());
        assertEquals("Estrutura de Dados", encontrada.get().getNome());
        assertEquals(60, encontrada.get().getCargaHoraria());
    }

    @Test
    void disciplinaInexistenteRetornaVazia() {
        assertTrue(new DisciplinaRepositoryJdbc(conexao).buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void disciplinaDuplicadaLancaExcecao() {
        var repositorio = new DisciplinaRepositoryJdbc(conexao);

        assertThrows(RegistroDuplicadoException.class, () ->
                repositorio.salvar(new Disciplina("POO", "Outra POO", 40)));
    }

    @Test
    void dadosContinuamDisponiveisComNovasInstancias() {
        // Simula encerrar e iniciar a aplicação: objetos novos, mesmo arquivo de banco
        new AlunoRepositoryJdbc(conexao)
                .salvar(new Aluno("RA2026100", "Maria Souza", "maria@email.com"));
        new DisciplinaRepositoryJdbc(conexao)
                .salvar(new Disciplina("ED", "Estrutura de Dados", 60));

        var novaConexao = new ConexaoBanco(url);

        assertTrue(new AlunoRepositoryJdbc(novaConexao).buscarPorRa("RA2026100").isPresent());
        assertTrue(new DisciplinaRepositoryJdbc(novaConexao).buscarPorCodigo("ED").isPresent());
    }

    @Test
    void erroDeBancoViraPersistenciaException() {
        // Banco vazio, sem tabelas: o SELECT/INSERT falha com SQLException
        var bancoSemTabelas = new ConexaoBanco(
                "jdbc:sqlite:" + pastaTemporaria.resolve("vazio.db"));

        assertThrows(PersistenciaException.class, () ->
                new AlunoRepositoryJdbc(bancoSemTabelas).buscarPorRa("RA2026001"));
        assertThrows(PersistenciaException.class, () ->
                new AlunoRepositoryJdbc(bancoSemTabelas)
                        .salvar(new Aluno("RA1", "Nome", "a@b.com")));
        assertThrows(PersistenciaException.class, () ->
                new DisciplinaRepositoryJdbc(bancoSemTabelas).buscarPorCodigo("POO"));
        assertThrows(PersistenciaException.class, () ->
                new DisciplinaRepositoryJdbc(bancoSemTabelas)
                        .salvar(new Disciplina("X", "Nome", 10)));
    }

    @Test
    void scriptAusenteGeraMensagemClara() {
        var bancoNovo = new ConexaoBanco("jdbc:sqlite:" + pastaTemporaria.resolve("novo.db"));
        var inicializador = new InicializadorBanco(bancoNovo, pastaTemporaria.resolve("nao-existe"));

        var erro = assertThrows(IllegalStateException.class, inicializador::inicializarSeNecessario);
        assertTrue(erro.getMessage().contains("projects/academico"));
    }
}
