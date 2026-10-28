package br.edu.sistemaacademico.repository.jdbc;

import br.edu.sistemaacademico.domain.Aluno;
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

class AlunoRepositoryJdbcTest {

    @TempDir
    Path pasta;

    private Path arquivoDoBanco;
    private AlunoRepositoryJdbc repository;

    @BeforeEach
    void prepararBanco() throws Exception {
        arquivoDoBanco = pasta.resolve("teste.db");
        repository = new AlunoRepositoryJdbc(BancoDeTeste.criarComTabelas(arquivoDoBanco));
    }

    @Test
    void deveSalvarEBuscarAlunoPorRa() {
        repository.salvar(new Aluno("RA100", "Ana Souza", "ana@email.com"));

        Optional<Aluno> encontrado = repository.buscarPorRa("RA100");

        assertTrue(encontrado.isPresent());
        assertEquals("RA100", encontrado.get().getRegistroAcademico());
        assertEquals("Ana Souza", encontrado.get().getNome());
        assertEquals("ana@email.com", encontrado.get().getEmail());
    }

    @Test
    void deveRetornarVazioQuandoAlunoNaoExiste() {
        assertTrue(repository.buscarPorRa("RA999").isEmpty());
    }

    @Test
    void deveRejeitarAlunoComRaDuplicado() {
        repository.salvar(new Aluno("RA100", "Ana Souza", "ana@email.com"));

        assertThrows(
                RegistroDuplicadoException.class,
                () -> repository.salvar(new Aluno("RA100", "Outra Pessoa", "outra@email.com"))
        );

        // O registro original permanece intacto.
        assertEquals("Ana Souza", repository.buscarPorRa("RA100").orElseThrow().getNome());
    }

    @Test
    void devePersistirAlunoAposNovaConexaoComOMesmoArquivo() {
        repository.salvar(new Aluno("RA100", "Ana Souza", "ana@email.com"));

        // Simula o reinício da aplicação: novos objetos sobre o mesmo arquivo.
        var repositoryReiniciado = new AlunoRepositoryJdbc(BancoDeTeste.semTabelas(arquivoDoBanco));

        assertEquals(
                "Ana Souza",
                repositoryReiniciado.buscarPorRa("RA100").orElseThrow().getNome()
        );
    }

    @Test
    void deveLancarErroDePersistenciaQuandoBancoNaoFoiPreparado() {
        var semTabelas = new AlunoRepositoryJdbc(BancoDeTeste.semTabelas(pasta.resolve("vazio.db")));

        assertThrows(ErroDePersistenciaException.class, () -> semTabelas.buscarPorRa("RA100"));
    }
}
