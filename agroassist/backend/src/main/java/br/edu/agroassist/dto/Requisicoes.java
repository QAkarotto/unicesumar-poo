package br.edu.agroassist.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Corpos JSON da API. A validação estrutural acontece antes dos casos de uso. */
public final class Requisicoes {
    private Requisicoes() {}

    public record DadosConta(
            @NotBlank(message = "Informe o nome completo.") @Size(max = 120) String nome,
            @NotBlank(message = "Informe o telefone.") @Size(max = 30) String telefone,
            @NotBlank(message = "Selecione o município.") @Size(max = 100) String municipio,
            @Email(message = "Informe um e-mail válido.") @Size(max = 160) String email,
            @NotNull(message = "Informe o consentimento.") Boolean consentimento) {}

    public record DadosPropriedade(
            @NotBlank(message = "Informe o nome da propriedade.") @Size(max = 120) String nome,
            @NotBlank(message = "Selecione o município.") @Size(max = 100) String municipio,
            @NotNull
                    @DecimalMin(value = "0", inclusive = false, message = "A área deve ser maior que zero.")
                    @Digits(integer = 10, fraction = 2, message = "A área deve ter até duas casas decimais.")
                    BigDecimal areaHa,
            @NotEmpty(message = "Marque pelo menos uma cultura.") List<@NotBlank String> culturaIds) {}

    public record DadosSolicitacao(
            @NotBlank String propriedadeId,
            @NotBlank String culturaId,
            @Size(max = 120) String estadio,
            @NotBlank(message = "Descreva o problema da lavoura.") @Size(max = 4000) String descricao,
            @NotNull @Size(max = 3, message = "Envie no máximo 3 fotos.")
                    List<@NotBlank @Size(max = 1_000_000) String> fotos) {}

    public record DadosTecnico(
            @NotBlank @Size(max = 120) String nome,
            @NotBlank @Size(max = 30) String telefone,
            @NotBlank @Size(max = 100) String municipio,
            @NotBlank @Size(max = 120) String formacao,
            @NotBlank @Size(max = 60) String registro,
            @NotEmpty(message = "Marque pelo menos uma cultura.") List<@NotBlank String> culturaIds) {}

    public record Atribuicao(@NotBlank String tecnicoId) {}

    public record Situacao(@NotNull Boolean ativo) {}

    public record DadosAtendimento(
            @NotNull(message = "Informe a data do atendimento.") LocalDate data,
            @NotBlank String modalidade,
            @NotBlank(message = "Descreva o diagnóstico.") @Size(max = 4000) String diagnostico,
            @NotBlank(message = "Descreva a recomendação.") @Size(max = 4000) String recomendacao) {}

    public record DadosAvaliacao(
            @NotNull
                    @Min(value = 1, message = "Escolha uma nota de 1 a 5.")
                    @Max(value = 5, message = "Escolha uma nota de 1 a 5.")
                    Integer nota,
            @Size(max = 2000) String comentario) {}
}
