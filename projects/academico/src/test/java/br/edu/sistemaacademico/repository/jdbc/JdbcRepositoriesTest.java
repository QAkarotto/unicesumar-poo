package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcRepositoriesTest {

    @TempDir
    Path pasta;

    private String url;
    private BancoDeDados banco;

    @BeforeEach
    void criarBanco() {
        url = "jdbc:sqlite:" + pasta.resolve("teste.db");
        banco = novoBanco();
    }

    private BancoDeDados novoBanco() {
        return new BancoDeDados(url, "database/schema.sql", "database/dados-iniciais.sql");
    }

    @Test
    void salvaEBuscaAluno() {
        var repositorio = new JdbcAlunoRepository(banco);
        repositorio.salvar(new Aluno("RA100", "Ana Lima", "ana@email.com"));

        var aluno = repositorio.buscarPorRa("RA100").orElseThrow();
        assertEquals("Ana Lima", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());
    }

    @Test
    void alunoInexistenteRetornaVazio() {
        assertTrue(new JdbcAlunoRepository(banco).buscarPorRa("RA404").isEmpty());
    }

    @Test
    void alunoDuplicadoLancaExcecao() {
        var repositorio = new JdbcAlunoRepository(banco);
        repositorio.salvar(new Aluno("RA100", "Ana Lima", "ana@email.com"));

        assertThrows(RegistroDuplicadoException.class,
                () -> repositorio.salvar(new Aluno("RA100", "Outra Ana", "outra@email.com")));
    }

    @Test
    void salvaEBuscaDisciplina() {
        var repositorio = new JdbcDisciplinaRepository(banco);
        repositorio.salvar(new Disciplina("ALG", "Algoritmos", 60));

        var disciplina = repositorio.buscarPorCodigo("ALG").orElseThrow();
        assertEquals("Algoritmos", disciplina.getNome());
        assertEquals(60, disciplina.getCargaHoraria());
    }

    @Test
    void disciplinaInexistenteRetornaVazio() {
        assertTrue(new JdbcDisciplinaRepository(banco).buscarPorCodigo("XXX").isEmpty());
    }

    @Test
    void disciplinaDuplicadaLancaExcecao() {
        var repositorio = new JdbcDisciplinaRepository(banco);
        repositorio.salvar(new Disciplina("ALG", "Algoritmos", 60));

        assertThrows(RegistroDuplicadoException.class,
                () -> repositorio.salvar(new Disciplina("ALG", "Outra", 40)));
    }

    @Test
    void dadosPermanecemAposReabrirOBanco() {
        new JdbcAlunoRepository(banco).salvar(new Aluno("RA100", "Ana Lima", "ana@email.com"));
        new JdbcDisciplinaRepository(banco).salvar(new Disciplina("ALG", "Algoritmos", 60));

        // Simula reiniciar a aplicação: novo BancoDeDados sobre o mesmo arquivo.
        var reiniciado = novoBanco();

        assertTrue(new JdbcAlunoRepository(reiniciado).buscarPorRa("RA100").isPresent());
        assertTrue(new JdbcDisciplinaRepository(reiniciado).buscarPorCodigo("ALG").isPresent());
    }

    @Test
    void dadosIniciaisSaoCarregadosUmaSoVez() {
        var repositorio = new JdbcAlunoRepository(banco);
        assertTrue(repositorio.buscarPorRa("RA2026001").isPresent());

        novoBanco(); // reexecutar os scripts não pode falhar nem duplicar
        assertTrue(repositorio.buscarPorRa("RA2026002").isPresent());
    }
}
