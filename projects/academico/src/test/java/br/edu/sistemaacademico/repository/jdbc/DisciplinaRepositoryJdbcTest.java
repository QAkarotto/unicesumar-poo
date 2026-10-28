package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Disciplina;
import br.edu.sistemaacademico.repository.ErroDePersistenciaException;
import br.edu.sistemaacademico.repository.RegistroDuplicadoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisciplinaRepositoryJdbcTest {

    @TempDir
    Path pasta;

    private Path arquivoDoBanco;
    private DisciplinaRepositoryJdbc repository;

    @BeforeEach
    void prepararBanco() throws Exception {
        arquivoDoBanco = pasta.resolve("teste.db");
        repository = new DisciplinaRepositoryJdbc(BancoDeTeste.criarComTabelas(arquivoDoBanco));
    }

    @Test
    void deveSalvarEBuscarDisciplinaPorCodigo() {
        repository.salvar(new Disciplina("ALG", "Algoritmos", 60));

        Optional<Disciplina> encontrada = repository.buscarPorCodigo("ALG");

        assertTrue(encontrada.isPresent());
        assertEquals("ALG", encontrada.get().getCodigo());
        assertEquals("Algoritmos", encontrada.get().getNome());
        assertEquals(60, encontrada.get().getCargaHoraria());
    }

    @Test
    void deveRetornarVazioQuandoDisciplinaNaoExiste() {
        assertTrue(repository.buscarPorCodigo("XYZ").isEmpty());
    }

    @Test
    void deveRejeitarDisciplinaComCodigoDuplicado() {
        repository.salvar(new Disciplina("ALG", "Algoritmos", 60));

        assertThrows(
                RegistroDuplicadoException.class,
                () -> repository.salvar(new Disciplina("ALG", "Outra Disciplina", 40))
        );

        // O registro original permanece intacto.
        assertEquals("Algoritmos", repository.buscarPorCodigo("ALG").orElseThrow().getNome());
    }

    @Test
    void devePersistirDisciplinaAposNovaConexaoComOMesmoArquivo() {
        repository.salvar(new Disciplina("ALG", "Algoritmos", 60));

        // Simula o reinício da aplicação: novos objetos sobre o mesmo arquivo.
        var repositoryReiniciado = new DisciplinaRepositoryJdbc(BancoDeTeste.semTabelas(arquivoDoBanco));

        assertEquals(
                60,
                repositoryReiniciado.buscarPorCodigo("ALG").orElseThrow().getCargaHoraria()
        );
    }

    @Test
    void deveLancarErroDePersistenciaQuandoBancoNaoFoiPreparado() {
        var semTabelas = new DisciplinaRepositoryJdbc(BancoDeTeste.semTabelas(pasta.resolve("vazio.db")));

        assertThrows(ErroDePersistenciaException.class, () -> semTabelas.buscarPorCodigo("ALG"));
    }
}
