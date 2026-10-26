package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import br.edu.sistemaacademico.support.BancoTeste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcRepositoryTest {

    @TempDir
    Path diretorio;

    private ConexaoBanco banco;
    private JdbcAlunoRepository alunos;
    private JdbcDisciplinaRepository disciplinas;

    @BeforeEach
    void prepararBanco() throws Exception {
        banco = BancoTeste.preparar(diretorio.resolve("academico.db"));
        alunos = new JdbcAlunoRepository(banco);
        disciplinas = new JdbcDisciplinaRepository(banco);
    }

    @Test
    void devePersistirAlunoEntreNovasInstanciasDeConexaoERepository() {
        var aluno = new Aluno("RA-NOVO", "Maria de Souza", "maria@email.com");
        assertEquals(aluno, alunos.salvar(aluno));

        var novaConexao = new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("academico.db"));
        var recuperado = new JdbcAlunoRepository(novaConexao).buscarPorRa("ra-novo").orElseThrow();

        assertEquals("RA-NOVO", recuperado.getRegistroAcademico());
        assertEquals("Maria de Souza", recuperado.getNome());
        assertEquals("maria@email.com", recuperado.getEmail());
    }

    @Test
    void devePersistirDisciplinaEntreNovasInstanciasDeConexaoERepository() {
        var disciplina = new Disciplina("REDES", "Redes de Computadores", 60);
        assertEquals(disciplina, disciplinas.salvar(disciplina));

        var novaConexao = new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("academico.db"));
        var recuperada = new JdbcDisciplinaRepository(novaConexao)
                .buscarPorCodigo("redes").orElseThrow();

        assertEquals("REDES", recuperada.getCodigo());
        assertEquals("Redes de Computadores", recuperada.getNome());
        assertEquals(60, recuperada.getCargaHoraria());
    }

    @Test
    void deveRetornarOptionalVazioParaRegistrosInexistentes() {
        assertTrue(alunos.buscarPorRa("RA-INEXISTENTE").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("INEXISTENTE").isEmpty());
    }

    @Test
    void deveRejeitarAlunoDuplicadoSemDiferenciarMaiusculas() {
        var erro = assertThrows(RegistroDuplicadoException.class,
                () -> alunos.salvar(new Aluno("ra2026001", "Outro nome", "outro@email.com")));

        assertInstanceOf(SQLException.class, erro.getCause());
        assertEquals("Paola Oliveira", alunos.buscarPorRa("RA2026001").orElseThrow().getNome());
    }

    @Test
    void deveRejeitarDisciplinaDuplicadaSemDiferenciarMaiusculas() {
        var erro = assertThrows(RegistroDuplicadoException.class,
                () -> disciplinas.salvar(new Disciplina("poo", "Outra disciplina", 40)));

        assertInstanceOf(SQLException.class, erro.getCause());
        assertEquals(80, disciplinas.buscarPorCodigo("POO").orElseThrow().getCargaHoraria());
    }

    @Test
    void deveTratarApostrofosETextoSqlComoDados() {
        var aluno = new Aluno("RA'01", "Ana D'Avila'); DROP TABLE alunos; --", "ana@email.com");
        var disciplina = new Disciplina("SQL'01", "SQL'); DROP TABLE disciplinas; --", 40);

        alunos.salvar(aluno);
        disciplinas.salvar(disciplina);

        assertEquals(aluno.getNome(), alunos.buscarPorRa("RA'01").orElseThrow().getNome());
        assertEquals(disciplina.getNome(), disciplinas.buscarPorCodigo("SQL'01").orElseThrow().getNome());
        assertTrue(alunos.buscarPorRa("' OR 1=1 --").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("' OR 1=1 --").isEmpty());
        assertTrue(alunos.buscarPorRa("RA2026001").isPresent());
        assertTrue(disciplinas.buscarPorCodigo("POO").isPresent());
    }

    @Test
    void deveInformarFalhaSqlQuandoATabelaDeAlunosNaoExiste() {
        var semTabelas = new JdbcAlunoRepository(
                new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("sem-tabelas.db")));

        var erroConsulta = assertThrows(PersistenciaException.class,
                () -> semTabelas.buscarPorRa("RA2026001"));
        var erroCadastro = assertThrows(PersistenciaException.class,
                () -> semTabelas.salvar(new Aluno("RA1", "Aluno", "aluno@email.com")));

        assertInstanceOf(SQLException.class, erroConsulta.getCause());
        assertInstanceOf(SQLException.class, erroCadastro.getCause());
    }

    @Test
    void deveInformarFalhaSqlQuandoATabelaDeDisciplinasNaoExiste() {
        var semTabelas = new JdbcDisciplinaRepository(
                new ConexaoBanco("jdbc:sqlite:" + diretorio.resolve("sem-tabelas.db")));

        var erroConsulta = assertThrows(PersistenciaException.class,
                () -> semTabelas.buscarPorCodigo("POO"));
        var erroCadastro = assertThrows(PersistenciaException.class,
                () -> semTabelas.salvar(new Disciplina("TESTE", "Disciplina", 40)));

        assertInstanceOf(SQLException.class, erroConsulta.getCause());
        assertInstanceOf(SQLException.class, erroCadastro.getCause());
    }
}
