package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoValido() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertEquals("A001", aluno.getRegistroAcademico());
        assertEquals("Douglas", aluno.getNome());
        assertEquals("douglas@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveCriarAlunoSemRegistroAcademico() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("", "Douglas", "douglas@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComRegistroAcademicoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno(null, "Douglas", "douglas@email.com"));
    }

    @Test
    void naoDeveCriarAlunoSemNome() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("A001", "", "douglas@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComNomeNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("A001", null, "douglas@email.com"));
    }

    @Test
    void naoDeveCriarAlunoComEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("A001", "Douglas", "email-invalido"));
    }

    @Test
    void naoDeveCriarAlunoComEmailNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aluno("A001", "Douglas", null));
    }

    @Test
    void deveAlterarEmail() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void naoDeveAlterarEmailParaInvalido() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertThrows(IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido"));
    }

    @Test
    void naoDeveAlterarEmailParaNulo() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertThrows(IllegalArgumentException.class,
                () -> aluno.setEmail(null));
    }

    @Test
    void deveAceitarEmailComFormatoValido() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        aluno.setEmail("novo.email@dominio.com");

        assertEquals("novo.email@dominio.com", aluno.getEmail());
    }

    @Test
    void deveRemoverEspacosDoRegistroAcademico() {
        Aluno aluno = new Aluno(
                "  A001  ",
                "Douglas",
                "douglas@email.com"
        );

        assertEquals("A001", aluno.getRegistroAcademico());
    }

    @Test
    void deveRemoverEspacosDoNome() {
        Aluno aluno = new Aluno(
                "A001",
                "  Douglas  ",
                "douglas@email.com"
        );

        assertEquals("Douglas", aluno.getNome());
    }

    @Test
    void deveCriarListaDeMatriculasVazia() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertNotNull(aluno.getMatriculas());
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    void deveRetornarListaDeMatriculasSomenteParaLeitura() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertThrows(
                UnsupportedOperationException.class,
                () -> aluno.getMatriculas().clear()
        );
    }

    @Test
    void alunosComMesmoRegistroAcademicoDevemSerIguais() {
        Aluno aluno1 = new Aluno("A001", "Douglas", "douglas@email.com");
        Aluno aluno2 = new Aluno("A001", "Outro Nome", "outro@email.com");

        assertEquals(aluno1, aluno2);
    }

    @Test
    void alunosComRegistrosAcademicosDiferentesNaoDevemSerIguais() {
        Aluno aluno1 = new Aluno("A001", "Douglas", "douglas@email.com");
        Aluno aluno2 = new Aluno("A002", "Douglas", "douglas@email.com");

        assertNotEquals(aluno1, aluno2);
    }

    @Test
    void alunoNaoDeveSerIgualANulo() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertNotEquals(aluno, null);
    }

    @Test
    void alunoNaoDeveSerIgualAOutroTipoDeObjeto() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertNotEquals(aluno, "A001");
    }

    @Test
    void devePossuirMesmoHashCodeParaMesmoRegistroAcademico() {
        Aluno aluno1 = new Aluno("A001", "Douglas", "douglas@email.com");
        Aluno aluno2 = new Aluno("A001", "Outro Nome", "outro@email.com");

        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }

    @Test
    void deveRetornarToStringComRegistroENome() {
        Aluno aluno = new Aluno("A001", "Douglas", "douglas@email.com");

        assertEquals("A001 - Douglas", aluno.toString());
    }

}