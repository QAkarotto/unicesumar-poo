package br.edu.controleestoque.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.controleestoque.domain.Categoria;
import br.edu.controleestoque.dto.CategoriaRequest;
import br.edu.controleestoque.repository.CategoriaRepository;
import br.edu.controleestoque.repository.ProdutoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaServiceTest {

    private CategoriaRepository categoriaRepository;
    private ProdutoRepository produtoRepository;
    private LogService logService;
    private CategoriaService service;

    @BeforeEach
    void preparar() {
        categoriaRepository = mock(CategoriaRepository.class);
        produtoRepository = mock(ProdutoRepository.class);
        logService = mock(LogService.class);
        service = new CategoriaService(categoriaRepository, produtoRepository, logService);
    }

    @Test
    void naoDevePermitirExcluirCategoriaComProdutos() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(new Categoria("Periféricos")));
        when(produtoRepository.existsByCategoriaId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.excluir(1L));

        verify(categoriaRepository, never()).delete(any());
        verify(logService, never()).registrar(any(), any(), any(), any());
    }

    @Test
    void naoDevePermitirNomeDeCategoriaDuplicado() {
        when(categoriaRepository.existsByNomeIgnoreCase("Periféricos")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.criar(new CategoriaRequest("Periféricos")));

        verify(categoriaRepository, never()).save(any());
    }
}
