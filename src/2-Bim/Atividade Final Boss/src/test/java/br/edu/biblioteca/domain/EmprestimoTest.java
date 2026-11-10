package br.edu.biblioteca.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmprestimoTest {

    @Test
    void naoDevePermitirDevolucaoDeEmprestimoJaDevolvido() {
        Leitor leitor = new Leitor("Ana", "ana@email.com");
        Livro livro = new Livro("978-3", "Refactoring", "Martin Fowler", 2);
        Emprestimo emprestimo = new Emprestimo(leitor, livro, LocalDate.of(2026, 1, 10));
        emprestimo.devolver(LocalDate.of(2026, 1, 20));

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> emprestimo.devolver(LocalDate.of(2026, 1, 21))
        );

        assertEquals("Somente empréstimos ativos podem ser devolvidos.", erro.getMessage());
        assertEquals(SituacaoEmprestimo.DEVOLVIDO, emprestimo.getSituacao());
        assertEquals(2, livro.getQuantidadeDisponivel());
    }

    @Test
    void deveDevolverERestaurarExemplar() {
        Leitor leitor = new Leitor("Bruno", "bruno@email.com");
        Livro livro = new Livro("978-4", "Effective Java", "Joshua Bloch", 1);
        Emprestimo emprestimo = new Emprestimo(leitor, livro, LocalDate.of(2026, 2, 1));

        emprestimo.devolver(LocalDate.of(2026, 2, 10));

        assertEquals(SituacaoEmprestimo.DEVOLVIDO, emprestimo.getSituacao());
        assertEquals(1, livro.getQuantidadeDisponivel());
    }
}
