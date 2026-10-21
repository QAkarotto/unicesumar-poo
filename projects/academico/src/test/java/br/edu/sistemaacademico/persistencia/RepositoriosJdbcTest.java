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
        var novo = new Aluno("RA2026003", "Aluno Novo", "novo@example.com");
        assertSame(novo, alunos.salvar(novo));
        disciplinas.salvar(new Disciplina("ES", "Engenharia de Software", 60));

        var aluno = new AlunoRepositoryJdbc(conexao).buscarPorRa("RA2026003").orElseThrow();
        assertEquals("Aluno Novo", aluno.getNome());
        assertEquals("novo@example.com", aluno.getEmail());
        assertEquals(60, new DisciplinaRepositoryJdbc(conexao).buscarPorCodigo("ES").orElseThrow().getCargaHoraria());
    }

    @Test
    void naoEncontraOQueNaoExiste() {
        assertTrue(alunos.buscarPorRa("RA0000000").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void recusaChaveRepetidaSemAlterarORegistroOriginal() {
        var repetido = new Aluno("RA2026001", "Outro Nome", "outro@example.com");
        assertThrows(IllegalStateException.class, () -> alunos.salvar(repetido));
        assertEquals("Aluno Exemplo Um", alunos.buscarPorRa("RA2026001").orElseThrow().getNome());
        assertThrows(IllegalStateException.class,
                () -> disciplinas.salvar(new Disciplina("POO", "Outra", 40)));
        assertEquals(80, disciplinas.buscarPorCodigo("POO").orElseThrow().getCargaHoraria());
    }

    @Test
    void emailRepetidoEhPermitido() {
        // o README diz que o e-mail e obrigatorio mas nao e unico
        alunos.salvar(new Aluno("RA2026003", "Aluno Tres", "aluno1@example.com"));
        assertEquals("aluno1@example.com", alunos.buscarPorRa("RA2026003").orElseThrow().getEmail());
        assertEquals("aluno1@example.com", alunos.buscarPorRa("RA2026001").orElseThrow().getEmail());
    }

    @Test
    void erroDoBancoViraPersistenciaException() {
        var semTabelas = new ConexaoBanco("jdbc:sqlite:" + pasta.resolve("vazio.db"));
        var erro = assertThrows(PersistenciaException.class, () -> new AlunoRepositoryJdbc(semTabelas).buscarPorRa("RA2026001"));
        assertEquals("Não foi possível consultar o aluno.", erro.getMessage());
        assertThrows(PersistenciaException.class, () -> new DisciplinaRepositoryJdbc(semTabelas).buscarPorCodigo("POO"));
    }
}
