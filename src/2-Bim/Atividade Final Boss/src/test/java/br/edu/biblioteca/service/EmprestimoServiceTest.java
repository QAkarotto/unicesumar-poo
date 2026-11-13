package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.domain.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class EmprestimoServiceTest {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LeitorService leitorService;

    @Autowired
    private LivroService livroService;

    @Test
    void naoDevePermitirMaisQueTresEmprestimosAtivos() {
        Leitor leitor = leitorService.cadastrar("Carla", "carla@email.com");
        Livro l1 = livroService.cadastrar("111", "Livro 1", "Autor", 1);
        Livro l2 = livroService.cadastrar("222", "Livro 2", "Autor", 1);
        Livro l3 = livroService.cadastrar("333", "Livro 3", "Autor", 1);
        Livro l4 = livroService.cadastrar("444", "Livro 4", "Autor", 1);

        emprestimoService.emprestar(leitor.getId(), l1.getId());
        emprestimoService.emprestar(leitor.getId(), l2.getId());
        emprestimoService.emprestar(leitor.getId(), l3.getId());

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> emprestimoService.emprestar(leitor.getId(), l4.getId())
        );

        assertEquals("Leitor já possui o limite de 3 empréstimos ativos.", erro.getMessage());
    }

    @Test
    void naoDevePermitirSegundoEmprestimoAtivoDoMesmoLivro() {
        Leitor leitor = leitorService.cadastrar("Diego", "diego@email.com");
        Livro livro = livroService.cadastrar("555", "Livro X", "Autor", 5);

        emprestimoService.emprestar(leitor.getId(), livro.getId());

        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> emprestimoService.emprestar(leitor.getId(), livro.getId())
        );

        assertEquals("Leitor já possui um empréstimo ativo deste livro.", erro.getMessage());
    }

    @Test
    void deveRegistrarDevolucao() {
        Leitor leitor = leitorService.cadastrar("Elena", "elena@email.com");
        Livro livro = livroService.cadastrar("666", "Livro Y", "Autor", 1);

        Emprestimo emprestimo = emprestimoService.emprestar(leitor.getId(), livro.getId());
        Emprestimo devolvido = emprestimoService.devolver(emprestimo.getId());

        assertEquals("DEVOLVIDO", devolvido.getSituacao().name());
        assertEquals(1, livroService.buscarPorId(livro.getId()).getQuantidadeDisponivel());
    }
}
