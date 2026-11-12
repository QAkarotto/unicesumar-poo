package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Tutor;
public record TutorResponse(Long id, String nome, String cpf, String telefone, String email) {
    public static TutorResponse from(Tutor t) {
        return new TutorResponse(t.getId(), t.getNome(), t.getCpf(), t.getTelefone(), t.getEmail());
    }
}