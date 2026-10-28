package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.BancoDeTeste;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.PersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa os Repositories JDBC diretamente, sem Spring e sem HTTP.
 */
class JdbcRepositoriesTest {
    private FabricaConexoes conexoes;
    private JdbcAlunoRepository alunos;
    private JdbcDisciplinaRepository disciplinas;

    @BeforeEach
    void criarBanco() throws Exception {
        conexoes = new FabricaConexoes(BancoDeTeste.criar());
        alunos = new JdbcAlunoRepository(conexoes);
        disciplinas = new JdbcDisciplinaRepository(conexoes);
    }

    @Test
    void salvaEReconstroiAlunoAPartirDoBanco() {
        alunos.salvar(new Aluno("RA2026100", "Carla Mendes", "carla.mendes@example.com"));

        var encontrado = new JdbcAlunoRepository(conexoes).buscarPorRa("RA2026100").orElseThrow();
        assertEquals("RA2026100", encontrado.getRegistroAcademico());
        assertEquals("Carla Mendes", encontrado.getNome());
        assertEquals("carla.mendes@example.com", encontrado.getEmail());
    }

    @Test
    void salvaEReconstroiDisciplinaAPartirDoBanco() {
        disciplinas.salvar(new Disciplina("ES", "Engenharia de Software", 60));

        var encontrada = new JdbcDisciplinaRepository(conexoes).buscarPorCodigo("ES").orElseThrow();
        assertEquals("Engenharia de Software", encontrada.getNome());
        assertEquals(60, encontrada.getCargaHoraria());
    }

    @Test
    void leDadosIniciais() {
        assertEquals("Aluno Exemplo Um", alunos.buscarPorRa("RA2026001").orElseThrow().getNome());
        assertEquals(80, disciplinas.buscarPorCodigo("BD").orElseThrow().getCargaHoraria());
    }

    @Test
    void retornaVazioQuandoNaoEncontra() {
        assertTrue(alunos.buscarPorRa("RA9999999").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void traduzChavePrimariaDuplicada() {
        assertThrows(RegistroDuplicadoException.class,
                () -> alunos.salvar(new Aluno("RA2026001", "Outro", "outro@example.com")));
        assertThrows(RegistroDuplicadoException.class,
                () -> disciplinas.salvar(new Disciplina("POO", "Outra", 40)));
        assertEquals("Aluno Exemplo Um", alunos.buscarPorRa("RA2026001").orElseThrow().getNome());
    }

    @Test
    void envolveOutrasFalhasDeSqlEmPersistenciaException() throws Exception {
        try (var conexao = conexoes.abrir(); var comando = conexao.createStatement()) {
            comando.executeUpdate("DROP TABLE aluno");
        }
        var erro = assertThrows(PersistenciaException.class, () -> alunos.buscarPorRa("RA2026001"));
        assertInstanceOf(java.sql.SQLException.class, erro.getCause());
    }
}
