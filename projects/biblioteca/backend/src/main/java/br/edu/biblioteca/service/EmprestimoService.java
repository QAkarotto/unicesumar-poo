package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.exception.*;
import br.edu.biblioteca.repository.*;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmprestimoService {
    private final LivroRepository livros;
    private final LeitorRepository leitores;
    private final EmprestimoRepository emprestimos;
    private final Clock clock;
    public EmprestimoService(LivroRepository livros, LeitorRepository leitores, EmprestimoRepository emprestimos, Clock clock) {
        this.livros = livros; this.leitores = leitores; this.emprestimos = emprestimos; this.clock = clock;
    }
    public List<EmprestimoResponse> listar() {
        var hoje = LocalDate.now(clock);
        return emprestimos.findAllByOrderByIdDesc().stream().map(e -> EmprestimoResponse.de(e, hoje)).toList();
    }
    public EmprestimoResponse buscar(Long id) {
        return EmprestimoResponse.de(emprestimos.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado.")), LocalDate.now(clock));
    }
    @Transactional
    public EmprestimoResponse criar(EmprestimoRequest r) {
        // A ordem leitor → livro é fixa. Os locks duram até o commit e protegem limites e estoque.
        var leitor = leitores.buscarComBloqueio(r.leitorId()).orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado."));
        var livro = livros.buscarComBloqueio(r.livroId()).orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado."));
        var hoje = LocalDate.now(clock);
        leitor.verificarElegibilidade(emprestimos.countByLeitorIdAndDevolvidoEmIsNull(leitor.getId()),
            emprestimos.existsByLeitorIdAndDevolvidoEmIsNullAndPrevistoParaBefore(leitor.getId(), hoje));
        if (emprestimos.existsByLeitorIdAndLivroIdAndDevolvidoEmIsNull(leitor.getId(), livro.getId())) {
            throw new RegraNegocioException("O leitor já possui um empréstimo ativo deste livro.");
        }
        livro.verificarDisponibilidade(emprestimos.countByLivroIdAndDevolvidoEmIsNull(livro.getId()));
        return EmprestimoResponse.de(emprestimos.save(new Emprestimo(livro, leitor, hoje)), hoje);
    }
    @Transactional
    public EmprestimoResponse devolver(Long id) {
        var e = emprestimos.buscarComBloqueio(id).orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado."));
        e.devolver(LocalDate.now(clock));
        return EmprestimoResponse.de(e, LocalDate.now(clock));
    }
}
