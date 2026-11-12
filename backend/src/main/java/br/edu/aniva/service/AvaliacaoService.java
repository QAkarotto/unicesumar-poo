package br.edu.aniva.service;

import br.edu.aniva.domain.Avaliacao;
import br.edu.aniva.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

/** Notas de 1 a 10. As respostas são mapas { idDaPessoa: nota }, como o frontend espera. */
@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioService usuarioService;
    private final ObraService obraService;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            UsuarioService usuarioService,
                            ObraService obraService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioService = usuarioService;
        this.obraService = obraService;
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> listar(String obraId) {
        obraService.obterObra(obraId);
        return notasDa(obraId);
    }

    // Regra: cada pessoa tem uma única nota por obra; avaliar de novo substitui a anterior.
    @Transactional
    public Map<Long, Integer> avaliar(Long usuarioId, String obraId, int nota) {
        var avaliacao = avaliacaoRepository.findByUsuarioIdAndObraId(usuarioId, obraId)
                .orElseGet(() -> new Avaliacao(
                        usuarioService.obterUsuario(usuarioId),
                        obraService.obterObra(obraId),
                        nota));
        avaliacao.alterarNota(nota);
        avaliacaoRepository.save(avaliacao);
        return notasDa(obraId);
    }

    @Transactional
    public Map<Long, Integer> remover(Long usuarioId, String obraId) {
        avaliacaoRepository.findByUsuarioIdAndObraId(usuarioId, obraId)
                .ifPresent(avaliacaoRepository::delete);
        return notasDa(obraId);
    }

    private Map<Long, Integer> notasDa(String obraId) {
        Map<Long, Integer> notas = new LinkedHashMap<>();
        for (Avaliacao avaliacao : avaliacaoRepository.findByObraId(obraId)) {
            notas.put(avaliacao.getUsuario().getId(), avaliacao.getNota());
        }
        return notas;
    }
}
