package br.edu.biblioteca.dto;
import br.edu.biblioteca.domain.Leitor;
public record LeitorResponse(Long id, String nome, String email, boolean ativo, long emprestimosAtivos) {
    public static LeitorResponse de(Leitor leitor, long ativos) {
        return new LeitorResponse(leitor.getId(), leitor.getNome(), leitor.getEmail(), leitor.isAtivo(), ativos);
    }
}
