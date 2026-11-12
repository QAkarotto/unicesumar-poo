package br.edu.seugame.domain;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class ClassificadorTabela {
    public List<Classificacao> recalcular(Torneio torneio) { return recalcular(torneio, List.of()); }

    public List<Classificacao> recalcular(Torneio torneio, Collection<Classificacao> existentes) {
        torneio.exigirEditavel();
        if (torneio.getTipo() != TipoTorneio.TABELA) throw new IllegalArgumentException("Classificação exige tabela.");
        torneio.validarParticipantes();
        var linhas = new LinkedHashMap<Long, Classificacao>();
        for (Classificacao linha : existentes) {
            if (!torneio.mesmoTorneio(linha.getTorneio()) || torneio.getParticipantes().stream()
                    .noneMatch(p -> p.mesmoParticipante(linha.getParticipante())))
                throw new IllegalArgumentException("Classificação externa à competição.");
            if (linhas.putIfAbsent(linha.getParticipante().getId(), linha) != null)
                throw new IllegalArgumentException("Classificação duplicada.");
        }
        for (Participante participante : torneio.getParticipantes()) {
            if (participante.getId() == null) throw new OperacaoNaoPermitidaException("Participante ainda não persistido.");
            linhas.computeIfAbsent(participante.getId(), id -> new Classificacao(torneio, participante));
        }
        linhas.values().forEach(Classificacao::zerarEstatisticas);
        for (Partida partida : torneio.getPartidas()) {
            if (partida.getFase() != FasePartida.LIGA) throw new OperacaoNaoPermitidaException("Estrutura incompatível com tabela.");
            if (!partida.temResultado()) continue;
            Classificacao primeiro = linhas.get(partida.getParticipante1().getId());
            Classificacao segundo = linhas.get(partida.getParticipante2().getId());
            if (primeiro == null || segundo == null) throw new OperacaoNaoPermitidaException("Partida com participante fora da lista.");
            primeiro.contabilizar(partida.getPlacar1(), partida.getPlacar2());
            segundo.contabilizar(partida.getPlacar2(), partida.getPlacar1());
        }
        return ordenar(linhas.values());
    }

    public List<Classificacao> ordenar(Collection<Classificacao> linhas) {
        Collator alfabetico = Collator.getInstance(Locale.forLanguageTag("pt-BR"));
        alfabetico.setStrength(Collator.SECONDARY);
        Comparator<Classificacao> criterio = Comparator.comparingInt(Classificacao::getPontos).reversed()
                .thenComparing(Comparator.comparingInt(Classificacao::getVitorias).reversed())
                .thenComparing(Comparator.comparingInt(Classificacao::getSaldo).reversed())
                .thenComparing(Comparator.comparingInt(Classificacao::getGolsPro).reversed())
                .thenComparing(linha -> linha.getParticipante().getNome(), alfabetico);
        return linhas.stream().sorted(criterio).toList();
    }

    public Participante determinarCampeao(Torneio torneio) {
        if (torneio.getPartidas().isEmpty() || torneio.getPartidas().stream().anyMatch(p -> !p.temResultado()))
            throw new OperacaoNaoPermitidaException("Tabela com resultados pendentes.");
        return recalcular(torneio).getFirst().getParticipante();
    }
}
