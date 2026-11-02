package br.edu.sistemaacademico.repository;

import br.edu.sistemaacademico.config.ConnectionFactory;
import br.edu.sistemaacademico.config.DatabaseInitializer;
import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.jdbc.AlunoRepositoryJdbc;
import br.edu.sistemaacademico.repository.jdbc.DisciplinaRepositoryJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryJdbcTest {

    private AlunoRepository alunoRepository;
    private DisciplinaRepository disciplinaRepository;

    @BeforeEach
    void setup() {
        ConnectionFactory connectionFactory = new ConnectionFactory("jdbc:sqlite:database/sistema-academico.db");
        DatabaseInitializer initializer = new DatabaseInitializer(connectionFactory);
        initializer.inicializarBanco();

        alunoRepository = new AlunoRepositoryJdbc(connectionFactory);
        disciplinaRepository = new DisciplinaRepositoryJdbc(connectionFactory);
    }

    @Test
    void operaAlunoRepositoryComSucesso() {
        String ra = "RA-REPO-" + System.currentTimeMillis();
        Aluno aluno = new Aluno(ra, "Aluno Teste Repo", "teste.repo@example.com");

        assertFalse(alunoRepository.existePorRa(ra));
        Aluno salvo = alunoRepository.salvar(aluno);
        assertEquals(aluno.getRegistroAcademico(), salvo.getRegistroAcademico());

        assertTrue(alunoRepository.existePorRa(ra));

        Optional<Aluno> buscado = alunoRepository.buscarPorRa(ra);
        assertTrue(buscado.isPresent());
        assertEquals("Aluno Teste Repo", buscado.get().getNome());
        assertEquals("teste.repo@example.com", buscado.get().getEmail());

        List<Aluno> todos = alunoRepository.listarTodos();
        assertFalse(todos.isEmpty());
    }

    @Test
    void operaDisciplinaRepositoryComSucesso() {
        String cod = "DISC-" + System.currentTimeMillis();
        Disciplina disciplina = new Disciplina(cod, "Disciplina Teste Repo", 60);

        assertFalse(disciplinaRepository.existePorCodigo(cod));
        Disciplina salva = disciplinaRepository.salvar(disciplina);
        assertEquals(disciplina.getCodigo(), salva.getCodigo());

        assertTrue(disciplinaRepository.existePorCodigo(cod));

        Optional<Disciplina> buscada = disciplinaRepository.buscarPorCodigo(cod);
        assertTrue(buscada.isPresent());
        assertEquals("Disciplina Teste Repo", buscada.get().getNome());
        assertEquals(60, buscada.get().getCargaHoraria());

        List<Disciplina> todas = disciplinaRepository.listarTodas();
        assertFalse(todas.isEmpty());
    }

    @Test
    void retornaVazioParaRegistrosInexistentes() {
        assertFalse(alunoRepository.buscarPorRa("INEXISTENTE_RA").isPresent());
        assertFalse(alunoRepository.buscarPorRa(null).isPresent());
        assertFalse(alunoRepository.buscarPorRa("   ").isPresent());
        assertFalse(alunoRepository.existePorRa("INEXISTENTE_RA"));
        assertFalse(alunoRepository.existePorRa(null));

        assertFalse(disciplinaRepository.buscarPorCodigo("INEXISTENTE_COD").isPresent());
        assertFalse(disciplinaRepository.buscarPorCodigo(null).isPresent());
        assertFalse(disciplinaRepository.buscarPorCodigo("   ").isPresent());
        assertFalse(disciplinaRepository.existePorCodigo("INEXISTENTE_COD"));
        assertFalse(disciplinaRepository.existePorCodigo(null));
    }
}
