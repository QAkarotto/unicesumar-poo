package br.edu.biblioteca.dto;

import br.edu.biblioteca.domain.Emprestimo;
import java.time.LocalDate;

public record EmprestimoResponse(
        Long id,
        Long usuarioId,
        Long livroId,
        String situacao,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucao) {

    public static EmprestimoResponse from(Emprestimo e) {
        return new EmprestimoResponse(
                e.getId(),
                e.getUsuario().getId(),
                e.getLivro().getId(),
                e.getSituacao().name(),
                e.getDataEmprestimo(),
                e.getDataDevolucao());
    }
}