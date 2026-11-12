package br.edu.seugame.dto;

import br.edu.seugame.domain.*;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.List;

public final class TorneioDtos {
    private TorneioDtos() {}
    public record TorneioRequest(@NotBlank @Size(min=3,max=120) String nome,
            @Size(max=2000) String descricao, @NotNull TipoTorneio tipo, @NotNull LocalDate dataInicio,
            @NotNull @Size(min=3) List<@NotBlank @Size(max=100) String> participantes) {}
    public record ResultadoRequest(@NotNull @Min(0) @Max(999) Integer placar1,
            @NotNull @Min(0) @Max(999) Integer placar2) {}
    public record CriadorResponse(Long id,String nome) {}
    public record ParticipanteResponse(Long id,String nome,int ordemSorteio) {}
    public record ResumoResponse(Long id,String nome,String descricao,TipoTorneio tipo,StatusTorneio status,
            LocalDate dataInicio,LocalDateTime criadoEm,CriadorResponse criador,ParticipanteResponse campeao,int totalParticipantes) {}
    public record TorneioResponse(ResumoResponse torneio,List<ParticipanteResponse> participantes) {}
    public record PartidaResponse(Long id,int rodada,int ordem,FasePartida fase,ParticipanteResponse participante1,
            ParticipanteResponse participante2,Short placar1,Short placar2,ParticipanteResponse vencedor) {}
    public record ClassificacaoResponse(int posicao,ParticipanteResponse participante,int jogos,int vitorias,int empates,
            int derrotas,int golsPro,int golsContra,int saldo,int pontos) {}
    public record RodadaResponse(int rodada,String nome,List<PartidaResponse> partidas) {}
}
