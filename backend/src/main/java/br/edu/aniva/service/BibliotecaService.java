package br.edu.aniva.service;

import br.edu.aniva.domain.ItemLista;
import br.edu.aniva.domain.StatusPauta;
import br.edu.aniva.dto.BibliotecaResponse;
import br.edu.aniva.dto.PautaRequest;
import br.edu.aniva.repository.ItemListaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Favoritos e pauta ("para ver/ler") da pessoa logada.
@Service
public class BibliotecaService {

    private final ItemListaRepository itemListaRepository;
    private final UsuarioService usuarioService;
    private final ObraService obraService;

    public BibliotecaService(ItemListaRepository itemListaRepository,
                             UsuarioService usuarioService,
                             ObraService obraService) {
        this.itemListaRepository = itemListaRepository;
        this.usuarioService = usuarioService;
        this.obraService = obraService;
    }

    @Transactional(readOnly = true)
    public BibliotecaResponse buscar(Long usuarioId) {
        return BibliotecaResponse.de(itemListaRepository.findByUsuarioId(usuarioId));
    }

    @Transactional
    public BibliotecaResponse alternarFavorito(Long usuarioId, String obraId) {
        ItemLista item = itemDe(usuarioId, obraId);
        item.alternarFavorito();
        return salvar(usuarioId, item);
    }

    @Transactional
    public BibliotecaResponse alternarPauta(Long usuarioId, String obraId) {
        ItemLista item = itemDe(usuarioId, obraId);
        item.alternarPauta();
        return salvar(usuarioId, item);
    }

    @Transactional
    public BibliotecaResponse atualizarPauta(Long usuarioId, String obraId, PautaRequest request) {
        StatusPauta novoStatus = request.status() == null ? null : StatusPauta.doTexto(request.status());
        ItemLista item = itemDe(usuarioId, obraId);
        item.atualizarPauta(novoStatus, request.progresso());
        return salvar(usuarioId, item);
    }

    // Busca o item da pessoa para a obra ou cria um novo (ainda não salvo).
    private ItemLista itemDe(Long usuarioId, String obraId) {
        return itemListaRepository.findByUsuarioIdAndObraId(usuarioId, obraId)
                .orElseGet(() -> new ItemLista(
                        usuarioService.obterUsuario(usuarioId),
                        obraService.obterObra(obraId)));
    }

    // Item sem favorito e fora da pauta não precisa existir no banco.
    private BibliotecaResponse salvar(Long usuarioId, ItemLista item) {
        if (item.estaVazio()) {
            if (item.getId() != null) {
                itemListaRepository.delete(item);
            }
        } else {
            itemListaRepository.save(item);
        }
        return BibliotecaResponse.de(itemListaRepository.findByUsuarioId(usuarioId));
    }
}
