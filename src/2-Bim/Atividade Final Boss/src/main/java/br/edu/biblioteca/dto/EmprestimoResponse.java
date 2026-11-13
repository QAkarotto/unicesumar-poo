package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Emprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(
        Long id,
        Long leitorId,
        String leitorNome,
        Long livroId,
        String livroTitulo,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        String situacao
) {
    public static EmprestimoResponse from(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getLeitor().getId(),
                emprestimo.getLeitor().getNome(),
                emprestimo.getLivro().getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getSituacao().name()
        );
    }
}
