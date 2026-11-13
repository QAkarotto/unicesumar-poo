package br.edu.biblioteca.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LivroTest {

    @Test
    void naoDeveEmprestarSemExemplaresDisponiveis() {
        Livro livro = new Livro("978-1", "Clean Code", "Uncle Bob", 1);
        livro.emprestarExemplar();

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                livro::emprestarExemplar
        );

        assertEquals("Não há exemplares disponíveis para empréstimo.", erro.getMessage());
        assertEquals(0, livro.getQuantidadeDisponivel());
    }

    @Test
    void deveReduzirQuantidadeAoEmprestar() {
        Livro livro = new Livro("978-2", "DDD", "Eric Evans", 3);

        livro.emprestarExemplar();

        assertEquals(2, livro.getQuantidadeDisponivel());
    }
}
