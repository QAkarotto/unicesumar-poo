package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoTest {

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("RA2026001", "Paola Oliveira", "paola.oliveira@email.com");
    }

    @Test
    @DisplayName("Deve criar aluno válido com os dados informados")
    void deveCriarAlunoValido() {
        assertEquals("RA2026001", aluno.getRegistroAcademico());
        assertEquals("Paola Oliveira", aluno.getNome());
        assertEquals("paola.oliveira@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Não deve criar aluno com registro acadêmico nulo ou em branco")
    void naoDeveCriarAlunoComRegistroInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Nome", "email@dominio.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("   ", "Nome", "email@dominio.com"));
    }

    @Test
    @DisplayName("Não deve criar aluno com nome nulo ou em branco")
    void naoDeveCriarAlunoComNomeInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA002", null, "email@dominio.com"));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA002", " ", "email@dominio.com"));
    }

    @Test
    @DisplayName("Não deve criar aluno com e-mail nulo ou em formato inválido")
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA003", "Nome", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("RA003", "Nome", "formato-invalido"));
    }

    @Test
    @DisplayName("Deve alterar o e-mail quando o novo valor for válido")
    void deveAlterarEmailValido() {
        // Act
        aluno.setEmail("novo.email@dominio.com");

        // Assert
        assertEquals("novo.email@dominio.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Não deve alterar o e-mail para um valor inválido, mantendo o anterior")
    void naoDeveAlterarEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> aluno.setEmail("invalido"));
        assertEquals("paola.oliveira@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("A lista de matrículas retornada deve ser imutável")
    void listaDeMatriculasDeveSerImutavel() {
        var matriculas = aluno.getMatriculas();
        assertThrows(UnsupportedOperationException.class, () -> matriculas.add(null));
    }

    @Test
    @DisplayName("Alunos com o mesmo registro acadêmico devem ser considerados iguais")
    void alunosComMesmoRegistroDevemSerIguais() {
        var outro = new Aluno("RA2026001", "Outro Nome", "outro@email.com");

        assertEquals(aluno, aluno);
        assertEquals(aluno, outro);
        assertEquals(aluno.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("Alunos com registros acadêmicos diferentes não devem ser iguais")
    void alunosComRegistrosDiferentesNaoDevemSerIguais() {
        var outro = new Aluno("RA2026002", "Paola Oliveira", "paola.oliveira@email.com");
        assertNotEquals(aluno, outro);
    }

    @Test
    @DisplayName("Aluno não deve ser igual a um objeto de outro tipo")
    void alunoNaoDeveSerIgualAObjetoDeOutroTipo() {
        assertNotEquals(aluno, "RA2026001");
    }

    @Test
    @DisplayName("toString deve apresentar o registro acadêmico e o nome do aluno")
    void toStringDeveConterRegistroENome() {
        assertEquals("RA2026001 - Paola Oliveira", aluno.toString());
    }
}