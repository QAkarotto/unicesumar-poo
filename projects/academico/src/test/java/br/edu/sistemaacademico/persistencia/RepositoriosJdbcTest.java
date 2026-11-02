package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriosJdbcTest {

    @TempDir
    Path pasta;

    private ConexaoBanco conexao;
    private AlunoRepository alunos;
    private DisciplinaRepository disciplinas;

    @BeforeEach
    void criarBanco() throws Exception {
        conexao = new ConexaoBanco(BancoDeTeste.criar(pasta));
        alunos = new AlunoRepositoryJdbc(conexao);
        disciplinas = new DisciplinaRepositoryJdbc(conexao);
    }

    @Test
    void leOsDadosIniciais() {
        var aluno = alunos.buscarPorRa("RA2026001").orElseThrow();
        assertEquals("Aluno Exemplo Um", aluno.getNome());
        assertEquals("aluno1@example.com", aluno.getEmail());

        var poo = disciplinas.buscarPorCodigo("POO").orElseThrow();
        assertEquals("Programação Orientada a Objetos", poo.getNome());
        assertEquals(80, poo.getCargaHoraria());
    }

    @Test
    void salvaEContinuaSalvoComOutraInstanciaDoRepository() {
        var novoAluno = new Aluno("RA2026003", "Aluno Novo", "novo@example.com");
        assertSame(novoAluno, alunos.salvar(novoAluno));

        var novaDisciplina = new Disciplina("ES", "Engenharia de Software", 60);
        assertSame(novaDisciplina, disciplinas.salvar(novaDisciplina));

        var alunoRecuperado = new AlunoRepositoryJdbc(conexao).buscarPorRa("RA2026003").orElseThrow();
        assertEquals("Aluno Novo", alunoRecuperado.getNome());
        assertEquals("novo@example.com", alunoRecuperado.getEmail());

        var disciplinaRecuperada = new DisciplinaRepositoryJdbc(conexao).buscarPorCodigo("ES").orElseThrow();
        assertEquals("Engenharia de Software", disciplinaRecuperada.getNome());
        assertEquals(60, disciplinaRecuperada.getCargaHoraria());
    }

    @Test
    void naoEncontraOQueNaoExiste() {
        assertTrue(alunos.buscarPorRa("RA0000000").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void recusaChaveRepetidaSemAlterarORegistroOriginal() {
        var alunoRepetido = new Aluno("RA2026001", "Outro Nome", "outro@example.com");
        assertThrows(IllegalStateException.class, () -> alunos.salvar(alunoRepetido));
        assertEquals("Aluno Exemplo Um", alunos.buscarPorRa("RA2026001").orElseThrow().getNome());

        var disciplinaRepetida = new Disciplina("POO", "Outra", 40);
        assertThrows(IllegalStateException.class, () -> disciplinas.salvar(disciplinaRepetida));
        assertEquals(80, disciplinas.buscarPorCodigo("POO").orElseThrow().getCargaHoraria());
    }

    @Test
    void emailRepetidoEhPermitido() {
        alunos.salvar(new Aluno("RA2026003", "Aluno Três", "aluno1@example.com"));
        assertEquals("aluno1@example.com", alunos.buscarPorRa("RA2026003").orElseThrow().getEmail());
        assertEquals("aluno1@example.com", alunos.buscarPorRa("RA2026001").orElseThrow().getEmail());
    }

    @Test
    void erroDoBancoViraPersistenciaException() {
        var semTabelas = new ConexaoBanco("jdbc:sqlite:" + pasta.resolve("vazio.db"));
        var erroAluno = assertThrows(PersistenciaException.class,
                () -> new AlunoRepositoryJdbc(semTabelas).buscarPorRa("RA2026001"));
        assertEquals("Não foi possível consultar o aluno.", erroAluno.getMessage());

        var erroDisciplina = assertThrows(PersistenciaException.class,
                () -> new DisciplinaRepositoryJdbc(semTabelas).buscarPorCodigo("POO"));
        assertEquals("Não foi possível consultar a disciplina.", erroDisciplina.getMessage());
    }
}
