package br.edu.aniva.dto;

import br.edu.aniva.domain.ItemLista;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Lista pessoal: ids dos favoritos e a pauta indexada pelo id da obra.
public record BibliotecaResponse(List<String> favoritos, Map<String, PautaItemResponse> pauta) {

    public record PautaItemResponse(String status, int progresso) {
    }

    public static BibliotecaResponse de(List<ItemLista> itens) {
        List<String> favoritos = itens.stream()
                .filter(ItemLista::isFavorito)
                .map(item -> item.getObra().getId())
                .toList();

        Map<String, PautaItemResponse> pauta = new LinkedHashMap<>();
        for (ItemLista item : itens) {
            if (item.naPauta()) {
                pauta.put(item.getObra().getId(),
                        new PautaItemResponse(item.getStatus().getTexto(), item.getProgresso()));
            }
        }
        return new BibliotecaResponse(favoritos, pauta);
    }
}
