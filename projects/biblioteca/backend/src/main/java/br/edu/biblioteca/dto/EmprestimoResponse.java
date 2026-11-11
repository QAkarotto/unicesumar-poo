package br.edu.biblioteca.dto;
import br.edu.biblioteca.domain.Emprestimo;
import java.time.LocalDate;
public record EmprestimoResponse(Long id, Long livroId, String livro, Long leitorId, String leitor,
    LocalDate emprestadoEm, LocalDate previstoPara, LocalDate devolvidoEm, String situacao) {
    public static EmprestimoResponse de(Emprestimo e, LocalDate hoje) {
        return new EmprestimoResponse(e.getId(), e.getLivro().getId(), e.getLivro().getTitulo(), e.getLeitor().getId(),
            e.getLeitor().getNome(), e.getEmprestadoEm(), e.getPrevistoPara(), e.getDevolvidoEm(), e.situacao(hoje));
    }
}
