package br.edu.aniva.dto;

import br.edu.aniva.domain.Obra;
import br.edu.aniva.domain.Plataforma;

import java.util.List;

// Obra no formato JSON esperado pelo frontend.
public record ObraResponse(
        String id,
        String title,
        String type,
        List<String> types,
        int year,
        String status,
        double rating,
        int popularity,
        Integer episodes,
        List<FormatoResponse> formats,
        List<String> genres,
        List<String> tropes,
        String studio,
        String author,
        String duration,
        List<String> warnings,
        PlataformasResponse platforms,
        List<PersonagemResponse> characters,
        String cover,
        String banner,
        String synopsis) {

    public record FormatoResponse(String type, String length, String note) {
    }

    public record LinkResponse(String name, String url) {
    }

    public record PlataformasResponse(List<LinkResponse> streaming, List<LinkResponse> reading, String note) {
    }

    public record PersonagemResponse(String name, String role, String description) {
    }

    public static ObraResponse de(Obra obra) {
        var formatos = obra.getFormatos().stream()
                .map(f -> new FormatoResponse(f.tipo(), f.extensao(), f.observacao()))
                .toList();

        var plataformas = new PlataformasResponse(
                links(obra, "streaming"),
                links(obra, "leitura"),
                obra.getPlataformasNota());

        var personagens = obra.getPersonagens().stream()
                .map(p -> new PersonagemResponse(p.nome(), p.papel(), p.descricao()))
                .toList();

        return new ObraResponse(
                obra.getId(), obra.getTitulo(), obra.getTipo(), List.copyOf(obra.getTipos()),
                obra.getAno(), obra.getStatus(), obra.getNota(), obra.getPopularidade(), obra.getEpisodios(),
                formatos, List.copyOf(obra.getGeneros()), List.copyOf(obra.getTropes()),
                obra.getEstudio(), obra.getAutor(), obra.getDuracao(), List.copyOf(obra.getAvisos()),
                plataformas, personagens, obra.getCapa(), obra.getBanner(), obra.getSinopse());
    }

    private static List<LinkResponse> links(Obra obra, String categoria) {
        return obra.getPlataformas().stream()
                .filter((Plataforma p) -> p.categoria().equals(categoria))
                .map(p -> new LinkResponse(p.nome(), p.url()))
                .toList();
    }
}
