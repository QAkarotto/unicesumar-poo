package br.com.vetagenda.dto.responses;
import br.com.vetagenda.domain.Especialidade;
public record EspecialidadeResponse(Long id, String nome, String descricao) {
    public static EspecialidadeResponse from(Especialidade e) {
        return new EspecialidadeResponse(e.getId(), e.getNome(), e.getDescricao());
    }
}