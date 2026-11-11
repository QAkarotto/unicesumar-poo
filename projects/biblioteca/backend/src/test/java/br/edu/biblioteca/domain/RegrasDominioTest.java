package br.edu.biblioteca.domain;

import br.edu.biblioteca.exception.RegraNegocioException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegrasDominioTest {
    private final LocalDate hoje = LocalDate.of(2026, 11, 11);
    private Livro livro(int quantidade) { return new Livro("Dom Casmurro", "Machado de Assis", "9788535914849", "Romance", quantidade); }
    private Leitor leitor() { return new Leitor("Ana", "ana@exemplo.com"); }

    @Test void naoEmprestaSemDisponibilidade() {
        assertThrows(RegraNegocioException.class, () -> livro(1).verificarDisponibilidade(1));
    }
    @Test void permiteUltimoExemplarDisponivel() {
        assertDoesNotThrow(() -> livro(2).verificarDisponibilidade(1));
    }
    @Test void naoReduzAcervoAbaixoDoTotalEmprestado() {
        var livro = livro(3);
        assertThrows(RegraNegocioException.class, () -> livro.atualizar("Dom Casmurro", "Machado", "9788535914849", "Romance", 1, 2));
    }
    @Test void naoPermiteQuartoEmprestimo() {
        assertThrows(RegraNegocioException.class, () -> leitor().verificarElegibilidade(3, false));
    }
    @Test void permiteTerceiroEmprestimo() { assertDoesNotThrow(() -> leitor().verificarElegibilidade(2, false)); }
    @Test void atrasoBloqueiaNovaRetirada() {
        assertThrows(RegraNegocioException.class, () -> leitor().verificarElegibilidade(1, true));
    }
    @Test void leitorInativoNaoPodeRetirarLivro() {
        var leitor = leitor(); leitor.alterarAtividade(false, 0);
        assertThrows(RegraNegocioException.class, () -> leitor.verificarElegibilidade(0, false));
    }
    @Test void naoInativaLeitorComEmprestimoPendente() {
        assertThrows(RegraNegocioException.class, () -> leitor().alterarAtividade(false, 1));
    }
    @Test void prazoDeQuatorzeDiasEInclusive() {
        var emprestimo = new Emprestimo(livro(1), leitor(), hoje);
        assertEquals(hoje.plusDays(14), emprestimo.getPrevistoPara());
        assertEquals("ATIVO", emprestimo.situacao(hoje.plusDays(14)));
        assertEquals("ATRASADO", emprestimo.situacao(hoje.plusDays(15)));
    }
    @Test void devolucaoEncerraAtraso() {
        var emprestimo = new Emprestimo(livro(1), leitor(), hoje);
        emprestimo.devolver(hoje.plusDays(20));
        assertEquals("DEVOLVIDO", emprestimo.situacao(hoje.plusDays(20)));
    }
    @Test void naoPermiteDuasDevolucoes() {
        var emprestimo = new Emprestimo(livro(1), leitor(), hoje); emprestimo.devolver(hoje);
        assertThrows(RegraNegocioException.class, () -> emprestimo.devolver(hoje));
    }
    @Test void naoPermiteDevolucaoAntesDaRetirada() {
        var emprestimo = new Emprestimo(livro(1), leitor(), hoje);
        assertThrows(RegraNegocioException.class, () -> emprestimo.devolver(hoje.minusDays(1)));
    }
}
