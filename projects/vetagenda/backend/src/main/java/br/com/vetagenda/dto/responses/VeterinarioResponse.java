package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Veterinario;
public record VeterinarioResponse(Long id, String nome, String crmv, String telefone,
                                  Long especialidadeId, String especialidade) {
    public static VeterinarioResponse from(Veterinario v) {
        return new VeterinarioResponse(v.getId(), v.getNome(), v.getCrmv(), v.getTelefone(),
            v.getEspecialidade().getId(), v.getEspecialidade().getNome());
    }
}