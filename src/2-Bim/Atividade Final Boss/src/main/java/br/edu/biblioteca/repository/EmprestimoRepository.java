package br.edu.biblioteca.repository;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    long countByLeitorIdAndSituacao(Long leitorId, SituacaoEmprestimo situacao);

    boolean existsByLeitorIdAndLivroIdAndSituacao(Long leitorId, Long livroId, SituacaoEmprestimo situacao);
}
