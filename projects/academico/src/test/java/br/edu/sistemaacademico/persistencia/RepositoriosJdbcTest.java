package br.edu.sistemaacademico.persistencia;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.List;
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
        alunos = new AlunoRepository(conexao);
        disciplinas = new DisciplinaRepository(conexao);
    }

    @Test
    void leOsDadosIniciais() {
        assertEquals(List.of("RA2026001", "RA2026002"),
                alunos.listarTodos().stream().map(Aluno::getRegistroAcademico).toList());
        assertEquals(List.of("BD", "POO"),
                disciplinas.listarTodas().stream().map(Disciplina::getCodigo).toList());
        var poo = disciplinas.buscarPorCodigo("POO").orElseThrow();
        assertEquals("Programação Orientada a Objetos", poo.getNome());
        assertEquals(80, poo.getCargaHoraria());
        assertEquals("aluno1@example.com", alunos.buscarPorRegistroAcademico("RA2026001").orElseThrow().getEmail());
    }

    @Test
    void salvaEContinuaSalvoComOutraInstanciaDoRepository() {
        alunos.salvar(new Aluno("RA2026003", "Aluno Novo", "novo@example.com"));
        disciplinas.salvar(new Disciplina("ES", "Engenharia de Software", 60));

        var aluno = new AlunoRepository(conexao).buscarPorRegistroAcademico("RA2026003").orElseThrow();
        assertEquals("Aluno Novo", aluno.getNome());
        assertEquals("novo@example.com", aluno.getEmail());
        assertEquals(60, new DisciplinaRepository(conexao).buscarPorCodigo("ES").orElseThrow().getCargaHoraria());
        assertEquals(3, alunos.listarTodos().size());
    }

    @Test
    void naoEncontraOQueNaoExiste() {
        assertTrue(alunos.buscarPorRegistroAcademico("RA0000000").isEmpty());
        assertTrue(disciplinas.buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void recusaChaveRepetidaSemAlterarORegistroOriginal() {
        var repetido = new Aluno("RA2026001", "Outro Nome", "outro@example.com");
        assertThrows(IllegalArgumentException.class, () -> alunos.salvar(repetido));
        assertEquals("Aluno Exemplo Um", alunos.buscarPorRegistroAcademico("RA2026001").orElseThrow().getNome());
        assertThrows(IllegalArgumentException.class,
                () -> disciplinas.salvar(new Disciplina("POO", "Outra", 40)));
    }

    @Test
    void emailRepetidoEhPermitido() {
        // o README diz que o e-mail e obrigatorio mas nao e unico
        alunos.salvar(new Aluno("RA2026003", "Aluno Tres", "aluno1@example.com"));
        assertEquals(3, alunos.listarTodos().size());
    }

    @Test
    void erroDoBancoViraPersistenciaException() {
        var semTabelas = new ConexaoBanco("jdbc:sqlite:" + pasta.resolve("vazio.db"));
        var erro = assertThrows(PersistenciaException.class, () -> new AlunoRepository(semTabelas).listarTodos());
        assertEquals("Não foi possível listar os alunos.", erro.getMessage());
        assertThrows(PersistenciaException.class, () -> new DisciplinaRepository(semTabelas).buscarPorCodigo("POO"));
    }
}
