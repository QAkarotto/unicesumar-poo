package br.edu.aniva.service;

import br.edu.aniva.Fabrica;
import br.edu.aniva.domain.Comentario;
import br.edu.aniva.domain.Obra;
import br.edu.aniva.domain.Usuario;
import br.edu.aniva.exception.OperacaoNaoPermitidaException;
import br.edu.aniva.exception.RecursoNaoEncontradoException;
import br.edu.aniva.repository.ComentarioRepository;
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

class ComentarioServiceTest {

    private final Usuario ana = Fabrica.usuario(1L, "Ana");
    private final Usuario bia = Fabrica.usuario(2L, "Bia");
    private final Obra frieren = Fabrica.obra("frieren");
    private final Obra onePiece = Fabrica.obra("one-piece");

    private ComentarioRepository repository;
    private ComentarioService service;
    private Comentario comentarioDaAna;

    @BeforeEach
    void preparar() {
        repository = mock(ComentarioRepository.class);
        var usuarioService = mock(UsuarioService.class);
        var obraService = mock(ObraService.class);
        when(usuarioService.obterUsuario(1L)).thenReturn(ana);
        when(usuarioService.obterUsuario(2L)).thenReturn(bia);
        when(obraService.obterObra("frieren")).thenReturn(frieren);

        comentarioDaAna = new Comentario(frieren, ana, "Gostei muito", false);
        when(repository.findById(10L)).thenReturn(Optional.of(comentarioDaAna));
        when(repository.findByObraIdOrderByCriadoEmAsc("frieren")).thenReturn(List.of(comentarioDaAna));

        service = new ComentarioService(repository, usuarioService, obraService);
    }

    @Test
    void naoDeveApagarComentarioDeOutraPessoa() {
        assertThrows(OperacaoNaoPermitidaException.class, () -> service.apagar(2L, "frieren", 10L));

        verify(repository, never()).delete(any());
    }

    @Test
    void autorDeveConseguirApagarOProprioComentario() {
        service.apagar(1L, "frieren", 10L);

        verify(repository).delete(comentarioDaAna);
    }

    @Test
    void naoDeveEncontrarComentarioPelaRotaDeOutraObra() {
        assertThrows(RecursoNaoEncontradoException.class, () -> service.apagar(1L, onePiece.getId(), 10L));
    }

    @Test
    void curtirDeveAparecerNaRespostaEAlternarDeNovoDeveRemover() {
        var aposCurtir = service.alternarCurtida(2L, "frieren", 10L);
        assertEquals(List.of(2L), aposCurtir.get(0).likes());

        var aposDescurtir = service.alternarCurtida(2L, "frieren", 10L);
        assertEquals(List.of(), aposDescurtir.get(0).likes());
    }
}
