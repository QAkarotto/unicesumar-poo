
package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoValido() {
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        assertEquals("RA2026001", aluno.getRegistroAcademico());
        assertEquals("Paola Oliveira", aluno.getNome());
        assertEquals("paola@email.com", aluno.getEmail());
        assertTrue(aluno.getMatriculas().isEmpty());
        assertEquals("RA2026001 - Paola Oliveira", aluno.toString());
    }

    @Test
    void deveAlterarEmail() {
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        aluno.setEmail("novo@email.com");

        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    void deveRejeitarEmailsInvalidos() {
        Aluno aluno = new Aluno(
                "RA2026001",
                "Paola Oliveira",
                "paola@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail(null)
        );
    }

    @Test
    void deveRejeitarRegistroAcademicoInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "Paola", "paola@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno(null, "Paola", "paola@email.com")
        );
    }

    @Test
    void deveRejeitarNomeInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "paola@email.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", null, "paola@email.com")
        );
    }

    @Test
    void deveCompararAlunosPeloRegistroAcademico() {
        Aluno primeiro = new Aluno(
                "RA001",
                "Paola",
                "paola@email.com"
        );

        Aluno segundo = new Aluno(
                "RA001",
                "Outro Nome",
                "outro@email.com"
        );

        Aluno terceiro = new Aluno(
                "RA002",
                "Bruno",
                "bruno@email.com"
        );

        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
        assertEquals(primeiro, primeiro);
        assertNotEquals(primeiro, terceiro);
        assertNotEquals(primeiro, null);
        assertNotEquals(primeiro, "RA001");
    }
    
@Test
void deveRemoverEspacosDoRegistroENome() {
    Aluno aluno = new Aluno(
            " RA001 ",
            " Paola Oliveira ",
            "paola@email.com"
    );

    assertEquals("RA001", aluno.getRegistroAcademico());
    assertEquals("Paola Oliveira", aluno.getNome());
    assertEquals("paola@email.com", aluno.getEmail());
}

}
