package br.com.vetagenda.dto.requests;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record TutorRequest(
    @NotBlank String nome,
    @NotBlank String cpf,
    @NotBlank String telefone,
    @Email String email
) {}