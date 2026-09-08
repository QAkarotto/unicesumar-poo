package br.edu.sistemaacademico.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    @DisplayName("Deve criar aluno com dados válidos")
    void deveCriarAluno() {

        // Cria um aluno utilizando dados válidos
        var aluno = new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );

        // Confirma que os dados foram armazenados corretamente
        assertEquals("RA001", aluno.getRegistroAcademico());
        assertEquals("Ana Souza", aluno.getNome());
        assertEquals("ana@email.com", aluno.getEmail());

        // Um aluno novo ainda não possui matrículas
        assertTrue(aluno.getMatriculas().isEmpty());
    }

    @Test
    @DisplayName("Não deve permitir registro acadêmico vazio")
    void naoDevePermitirRegistroVazio() {

        // Tenta criar um aluno sem informar o registro acadêmico
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("", "Ana Souza", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Não deve permitir nome vazio")
    void naoDevePermitirNomeVazio() {

        // Tenta criar um aluno sem informar o nome
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "", "ana@email.com")
        );
    }

    @Test
    @DisplayName("Não deve permitir e-mail inválido")
    void naoDevePermitirEmailInvalido() {

        // Tenta criar um aluno utilizando um e-mail fora do formato esperado
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aluno("RA001", "Ana Souza", "email-invalido")
        );
    }

    @Test
    @DisplayName("Deve permitir alteração de e-mail válido")
    void deveAlterarEmail() {

        // Cria um aluno com um e-mail inicial
        var aluno = new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );

        // Altera o e-mail para um endereço válido
        aluno.setEmail("novo@email.com");

        // Confirma que o novo e-mail foi armazenado
        assertEquals("novo@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Não deve permitir alteração para e-mail inválido")
    void naoDeveAlterarParaEmailInvalido() {

        // Cria um aluno com um e-mail válido
        var aluno = new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );

        // Tenta alterar o e-mail para um formato inválido
        assertThrows(
                IllegalArgumentException.class,
                () -> aluno.setEmail("email-invalido")
        );

        // Confirma que o e-mail antigo foi mantido
        assertEquals("ana@email.com", aluno.getEmail());
    }

    @Test
    @DisplayName("Alunos com mesmo registro acadêmico devem ser iguais")
    void deveCompararAlunosPeloRegistroAcademico() {

        // Cria dois alunos com o mesmo registro, mas com outros dados diferentes
        var aluno1 = new Aluno(
                "RA001",
                "Ana Souza",
                "ana@email.com"
        );

        var aluno2 = new Aluno(
                "RA001",
                "Outro Nome",
                "outro@email.com"
        );

        // Verifica que o registro acadêmico é utilizado para identificar o aluno
        assertEquals(aluno1, aluno2);

        // Objetos considerados iguais também devem possuir o mesmo hashCode
        assertEquals(aluno1.hashCode(), aluno2.hashCode());
    }
}