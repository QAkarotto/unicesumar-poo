package br.edu.aniva.service;

import br.edu.aniva.domain.Comentario;
import br.edu.aniva.dto.ComentarioRequest;
import br.edu.aniva.dto.ComentarioResponse;
import br.edu.aniva.exception.RecursoNaoEncontradoException;
import br.edu.aniva.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioService usuarioService;
    private final ObraService obraService;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             UsuarioService usuarioService,
                             ObraService obraService) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioService = usuarioService;
        this.obraService = obraService;
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listar(String obraId) {
        obraService.obterObra(obraId);
        return comentariosDa(obraId);
    }

    @Transactional
    public List<ComentarioResponse> comentar(Long usuarioId, String obraId, ComentarioRequest request) {
        var comentario = new Comentario(
                obraService.obterObra(obraId),
                usuarioService.obterUsuario(usuarioId),
                request.texto(),
                request.spoiler());
        comentarioRepository.save(comentario);
        return comentariosDa(obraId);
    }

    // Regra: curtir de novo remove a curtida (alterna).
    @Transactional
    public List<ComentarioResponse> alternarCurtida(Long usuarioId, String obraId, Long comentarioId) {
        Comentario comentario = buscarComentario(obraId, comentarioId);
        comentario.alternarCurtida(usuarioService.obterUsuario(usuarioId));
        return comentariosDa(obraId);
    }

    // Regra: só o autor pode apagar o próprio comentário.
    @Transactional
    public List<ComentarioResponse> apagar(Long usuarioId, String obraId, Long comentarioId) {
        Comentario comentario = buscarComentario(obraId, comentarioId);
        comentario.exigirAutor(usuarioService.obterUsuario(usuarioId));
        comentarioRepository.delete(comentario);
        return comentariosDa(obraId);
    }

    private Comentario buscarComentario(String obraId, Long comentarioId) {
        return comentarioRepository.findById(comentarioId)
                .filter(comentario -> comentario.getObra().getId().equals(obraId))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Comentário não encontrado."));
    }

    private List<ComentarioResponse> comentariosDa(String obraId) {
        return comentarioRepository.findByObraIdOrderByCriadoEmAsc(obraId).stream()
                .map(ComentarioResponse::de)
                .toList();
    }
}
