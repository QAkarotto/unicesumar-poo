package br.edu.seugame.dto;

import jakarta.validation.constraints.*;

public final class ContaDtos {
    private ContaDtos() {}
    public record CadastroRequest(@NotBlank @Size(min=3,max=100) String nome,
            @NotBlank @Pattern(regexp="[a-zA-Z0-9_.-]{3,50}") String usuario,
            @NotBlank @Email @Size(max=150) String email,
            @NotNull @Size(min=8,max=72) String senha, @NotNull String confirmarSenha) {}
    public record LoginRequest(@NotBlank String identificador, @NotNull String senha) {}
    public record PerfilRequest(@NotBlank @Size(min=3,max=100) String nome,
            @NotBlank @Email @Size(max=150) String email) {}
    public record UsuarioResponse(Long id, String nome, String usuario, String email,
            long totalTorneios, long finalizados) {}
    public record CsrfResponse(String headerName, String token) {}
    public record MeuTorneioResponse(Long id, String nome, br.edu.seugame.domain.TipoTorneio tipo,
            br.edu.seugame.domain.StatusTorneio status, java.time.LocalDate dataInicio,
            int totalParticipantes, long resultados, TorneioDtos.ParticipanteResponse campeao) {}
}
