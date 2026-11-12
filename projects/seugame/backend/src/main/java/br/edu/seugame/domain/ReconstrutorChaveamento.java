package br.edu.seugame.domain;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReconstrutorChaveamento {
    private record Posicao(int rodada, int ordem) {}

    public void reconstruir(Torneio torneio) {
        Map<Posicao, Partida> jogos = organizar(torneio);
        for (Partida partida : jogos.values()) {
            if (partida.getRodada() == 1) {
                partida.recomporConfronto(partida.getParticipante1(), partida.getParticipante2());
            } else {
                int anterior = partida.getRodada() - 1;
                Partida esquerda = jogos.get(new Posicao(anterior, partida.getOrdem() * 2 - 1));
                Partida direita = jogos.get(new Posicao(anterior, partida.getOrdem() * 2));
                partida.recomporConfronto(esquerda == null ? null : esquerda.getVencedor(),
                        direita == null ? null : direita.getVencedor());
            }
        }
    }

    private Map<Posicao, Partida> organizar(Torneio torneio) {
        torneio.exigirEditavel();
        if (torneio.getTipo() != TipoTorneio.CHAVEAMENTO) throw new IllegalArgumentException("Exige chaveamento.");
        Map<Posicao, Partida> jogos = new LinkedHashMap<>();
        for (Partida p : torneio.getPartidas().stream().sorted(Comparator.comparingInt(Partida::getRodada)
                .thenComparingInt(Partida::getOrdem)).toList()) {
            if (p.getFase() != FasePartida.CHAVE) throw new OperacaoNaoPermitidaException("Estrutura incompatível com chaveamento.");
            if (jogos.putIfAbsent(new Posicao(p.getRodada(),p.getOrdem()),p) != null)
                throw new OperacaoNaoPermitidaException("Posição repetida na chave.");
        }
        return jogos;
    }

    public Participante determinarCampeao(Torneio torneio) {
        torneio.validarParticipantes();
        reconstruir(torneio);
        var jogos = organizar(torneio);
        int tamanho = GeradorChaveamento.tamanhoChave(torneio.getParticipantes().size());
        if (jogos.size() != tamanho - 1) throw new OperacaoNaoPermitidaException("Chave incompleta.");
        Partida finalissima = null;
        for (int rodada = 1, quantidade = tamanho / 2; quantidade > 0; rodada++, quantidade /= 2) {
            for (int ordem = 1; ordem <= quantidade; ordem++) {
                Partida partida = jogos.get(new Posicao(rodada,ordem));
                if (partida == null || !partida.estaResolvida()) throw new OperacaoNaoPermitidaException("Chave com pendências.");
                if (quantidade == 1) finalissima = partida;
            }
        }
        if (finalissima == null || finalissima.getVencedor() == null) throw new OperacaoNaoPermitidaException("Final indefinida.");
        return finalissima.getVencedor();
    }
}
