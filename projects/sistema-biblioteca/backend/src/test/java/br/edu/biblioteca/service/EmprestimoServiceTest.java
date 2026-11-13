
package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import br.edu.biblioteca.repository.EmprestimoRepository;
import br.edu.biblioteca.repository.LeitorRepository;
import br.edu.biblioteca.repository.LivroRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LeitorRepository leitorRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Livro livro;
    private Leitor leitor;

    @BeforeEach
    void prepararDados() {

        livro = new Livro(
            "Dom Casmurro",
            "Machado de Assis",
            "9788535914849",
            5,
            5
        );

        leitor = new Leitor(
            "João Silva",
            "joao.silva@exemplo.com",
            "41999999999"
        );
    }

    @Test
    void naoDeveEmprestarLivroSemEstoque() {

        livro.setQuantidadeDisponivel(0);

        when(leitorRepository.buscarPorIdComBloqueio(1L))
            .thenReturn(Optional.of(leitor));

        when(livroRepository.buscarPorIdComBloqueio(1L))
            .thenReturn(Optional.of(livro));

        IllegalArgumentException erro = assertThrows(
            IllegalArgumentException.class,
            () -> emprestimoService.emprestarResponse(1L, 1L)
        );

        assertEquals(
            "Não há exemplares disponíveis deste livro.",
            erro.getMessage()
        );

        verify(emprestimoRepository, never())
            .save(any());
    }

    @Test
    void naoDeveUltrapassarTresEmprestimosAtivos() {

        when(leitorRepository.buscarPorIdComBloqueio(1L))
            .thenReturn(Optional.of(leitor));

        when(livroRepository.buscarPorIdComBloqueio(1L))
            .thenReturn(Optional.of(livro));

        when(emprestimoRepository.countByLeitorIdAndSituacao(
            1L,
            SituacaoEmprestimo.ATIVO
        )).thenReturn(3L);

        IllegalArgumentException erro = assertThrows(
            IllegalArgumentException.class,
            () -> emprestimoService.emprestarResponse(1L, 1L)
        );

        assertEquals(
            "O leitor já possui três empréstimos ativos.",
            erro.getMessage()
        );

        verify(emprestimoRepository, never())
            .save(any());
    }

    @Test
    void naoDeveEmprestarLivroInexistente() {

        when(leitorRepository.buscarPorIdComBloqueio(1L))
            .thenReturn(Optional.of(leitor));

        when(livroRepository.buscarPorIdComBloqueio(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            java.util.NoSuchElementException.class,
            () -> emprestimoService.emprestarResponse(999L, 1L)
        );

        verify(emprestimoRepository, never())
            .save(any());
    }
}

