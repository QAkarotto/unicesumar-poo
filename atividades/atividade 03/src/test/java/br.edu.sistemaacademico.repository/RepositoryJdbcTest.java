package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.infra.ConexaoSqlite;
import br.edu.sistemaacademico.infra.InicializadorBanco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testa os Repositories JDBC diretamente, sem Spring, contra um arquivo SQLite temporário. */
class RepositoryJdbcTest {

    @TempDir
    Path pasta;

    Path arquivoDoBanco;

    @BeforeEach
    void prepararBanco() {
        arquivoDoBanco = pasta.resolve("teste.db");
        new InicializadorBanco(new ConexaoSqlite(arquivoDoBanco.toString()))
                .executar(Path.of("database/schema.sql"));
    }

    private ConexaoSqlite novaConexao() {
        return new ConexaoSqlite(arquivoDoBanco.toString());
    }

    @Test
    void salvaEBuscaAluno() {
        var repo = new JdbcAlunoRepository(novaConexao());
        repo.salvar(new Aluno("RA1", "Ana", "ana@email.com"));

        var encontrado = repo.buscarPorRa("RA1").orElseThrow();
        assertEquals("Ana", encontrado.getNome());
        assertEquals("ana@email.com", encontrado.getEmail());
    }

    @Test
    void alunoInexistenteRetornaVazio() {
        assertTrue(new JdbcAlunoRepository(novaConexao()).buscarPorRa("NAO-EXISTE").isEmpty());
    }

    @Test
    void alunoDuplicadoLancaExcecao() {
        var repo = new JdbcAlunoRepository(novaConexao());
        repo.salvar(new Aluno("RA1", "Ana", "ana@email.com"));
        assertThrows(RegistroDuplicadoException.class,
                () -> repo.salvar(new Aluno("RA1", "Outra", "outra@email.com")));
    }

    @Test
    void salvaEBuscaDisciplina() {
        var repo = new JdbcDisciplinaRepository(novaConexao());
        repo.salvar(new Disciplina("ALG", "Algoritmos", 60));

        var encontrada = repo.buscarPorCodigo("ALG").orElseThrow();
        assertEquals("Algoritmos", encontrada.getNome());
        assertEquals(60, encontrada.getCargaHoraria());
    }

    @Test
    void disciplinaDuplicadaLancaExcecao() {
        var repo = new JdbcDisciplinaRepository(novaConexao());
        repo.salvar(new Disciplina("ALG", "Algoritmos", 60));
        assertThrows(RegistroDuplicadoException.class,
                () -> repo.salvar(new Disciplina("ALG", "Outra", 30)));
    }

    /** Simula "reiniciar a aplicação": novas conexões e novos repositórios sobre o mesmo arquivo. */
    @Test
    void dadosPersistemAposReiniciar() {
        new JdbcAlunoRepository(novaConexao()).salvar(new Aluno("RA9", "Eva", "eva@email.com"));
        new JdbcDisciplinaRepository(novaConexao()).salvar(new Disciplina("SO", "Sistemas Operacionais", 80));

        // "segunda execução"
        var alunos = new JdbcAlunoRepository(novaConexao());
        var disciplinas = new JdbcDisciplinaRepository(novaConexao());

        assertEquals("Eva", alunos.buscarPorRa("RA9").orElseThrow().getNome());
        assertEquals(80, disciplinas.buscarPorCodigo("SO").orElseThrow().getCargaHoraria());
    }

    @Test
    void scriptsIniciaisSaoIdempotentes() {
        var inicializador = new InicializadorBanco(novaConexao());
        var scripts = new Path[]{Path.of("database/schema.sql"), Path.of("database/dados-iniciais.sql")};
        inicializador.executar(scripts);
        inicializador.executar(scripts); // segunda vez não deve falhar nem duplicar

        assertTrue(new JdbcAlunoRepository(novaConexao()).buscarPorRa("RA2026001").isPresent());
        assertTrue(new JdbcDisciplinaRepository(novaConexao()).buscarPorCodigo("POO").isPresent());
    }
}
