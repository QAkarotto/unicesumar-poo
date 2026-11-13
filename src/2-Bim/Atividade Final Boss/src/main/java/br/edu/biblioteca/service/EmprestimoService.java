package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import br.edu.biblioteca.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    public static final int LIMITE_EMPRESTIMOS_ATIVOS = 3;

    private final EmprestimoRepository emprestimoRepository;
    private final LeitorService leitorService;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LeitorService leitorService,
                             LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.leitorService = leitorService;
        this.livroService = livroService;
    }

    @Transactional
    public Emprestimo emprestar(Long leitorId, Long livroId) {
        Leitor leitor = leitorService.buscarPorId(leitorId);
        Livro livro = livroService.buscarPorId(livroId);

        long ativos = emprestimoRepository.countByLeitorIdAndSituacao(leitorId, SituacaoEmprestimo.ATIVO);
        if (ativos >= LIMITE_EMPRESTIMOS_ATIVOS) {
            throw new IllegalStateException("Leitor já possui o limite de " + LIMITE_EMPRESTIMOS_ATIVOS + " empréstimos ativos.");
        }

        if (emprestimoRepository.existsByLeitorIdAndLivroIdAndSituacao(leitorId, livroId, SituacaoEmprestimo.ATIVO)) {
            throw new IllegalStateException("Leitor já possui um empréstimo ativo deste livro.");
        }

        return emprestimoRepository.save(new Emprestimo(leitor, livro, LocalDate.now()));
    }

    @Transactional
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = buscarPorId(id);
        emprestimo.devolver(LocalDate.now());
        return emprestimo;
    }

    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado: " + id));
    }
}
