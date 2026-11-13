
package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.SituacaoEmprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(
    Long id,
    Long livroId,
    String tituloLivro,
    Long leitorId,
    String nomeLeitor,
    LocalDate dataEmprestimo,
    LocalDate dataPrevistaDevolucao,
    LocalDate dataDevolucao,
    SituacaoEmprestimo situacao
) {

    public static EmprestimoResponse fromEntity(
            Emprestimo emprestimo) {

        return new EmprestimoResponse(
            emprestimo.getId(),
            emprestimo.getLivro().getId(),
            emprestimo.getLivro().getTitulo(),
            emprestimo.getLeitor().getId(),
            emprestimo.getLeitor().getNome(),
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataPrevistaDevolucao(),
            emprestimo.getDataDevolucao(),
            emprestimo.getSituacao()
        );
    }
}
