package br.edu.aniva.service;

import br.edu.aniva.Fabrica;
import br.edu.aniva.domain.Avaliacao;
import br.edu.aniva.domain.Obra;
import br.edu.aniva.domain.Usuario;
import br.edu.aniva.repository.AvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AvaliacaoServiceTest {

    private final Usuario ana = Fabrica.usuario(1L, "Ana");
    private final Obra obra = Fabrica.obra("frieren");

    private AvaliacaoRepository repository;
    private AvaliacaoService service;

    @BeforeEach
    void preparar() {
        repository = mock(AvaliacaoRepository.class);
        var usuarioService = mock(UsuarioService.class);
        var obraService = mock(ObraService.class);
        when(usuarioService.obterUsuario(1L)).thenReturn(ana);
        when(obraService.obterObra("frieren")).thenReturn(obra);
        service = new AvaliacaoService(repository, usuarioService, obraService);
    }

    @Test
    void avaliarDeNovoDeveSubstituirANotaAnteriorEmVezDeCriarOutra() {
        var existente = new Avaliacao(ana, obra, 5);
        when(repository.findByUsuarioIdAndObraId(1L, "frieren")).thenReturn(Optional.of(existente));
        when(repository.findByObraId("frieren")).thenReturn(List.of(existente));

        var notas = service.avaliar(1L, "frieren", 9);

        assertEquals(9, existente.getNota());
        assertEquals(1, notas.size());
        assertEquals(9, notas.get(1L));
    }

    @Test
    void notaInvalidaNaoDeveSerSalva() {
        when(repository.findByUsuarioIdAndObraId(1L, "frieren")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.avaliar(1L, "frieren", 11));

        verify(repository, never()).save(any());
    }
}
