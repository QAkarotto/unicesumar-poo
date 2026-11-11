package br.edu.biblioteca.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class EmprestimoTest {

    @Test
    void naoDevePermitirQuartoEmprestimoAtivo() {
        assertThrows(IllegalStateException.class,
                () -> Emprestimo.validarNovo(3, false));
    }

    @Test
    void naoDevePermitirEmprestarLivroJaEmprestado() {
        assertThrows(IllegalStateException.class,
                () -> Emprestimo.validarNovo(0, true));
    }

    @Test
    void devolucaoMudaSituacaoParaDevolvido() {
        Emprestimo e = new Emprestimo(
                new Usuario("Ana", "ana@example.com"),
                new Livro("Dom Casmurro", "Machado de Assis", "111"),
                LocalDate.of(2026, 11, 1));

        e.devolver(LocalDate.of(2026, 11, 5));

        assertEquals(SituacaoEmprestimo.DEVOLVIDO, e.getSituacao());
    }

    @Test
    void naoDevePermitirDevolverDuasVezes() {
        Emprestimo e = new Emprestimo(
                new Usuario("Ana", "ana@example.com"),
                new Livro("Dom Casmurro", "Machado de Assis", "111"),
                LocalDate.of(2026, 11, 1));
        e.devolver(LocalDate.of(2026, 11, 5));

        assertThrows(IllegalStateException.class,
                () -> e.devolver(LocalDate.of(2026, 11, 6)));
    }
}