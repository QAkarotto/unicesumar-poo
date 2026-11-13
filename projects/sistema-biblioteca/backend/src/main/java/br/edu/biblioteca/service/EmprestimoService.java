
package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.Leitor;
import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import br.edu.biblioteca.dto.EmprestimoResponse;

import br.edu.biblioteca.repository.EmprestimoRepository;
import br.edu.biblioteca.repository.LeitorRepository;
import br.edu.biblioteca.repository.LivroRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmprestimoService {

    private static final int LIMITE_EMPRESTIMOS = 3;
    private static final int PRAZO_DIAS = 14;

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final LeitorRepository leitorRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            LivroRepository livroRepository,
            LeitorRepository leitorRepository) {

        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.leitorRepository = leitorRepository;
    }

    @Transactional
    public EmprestimoResponse emprestarResponse(
            Long livroId, Long leitorId) {

        if (livroId == null || leitorId == null) {
            throw new IllegalArgumentException(
                "Livro e leitor são obrigatórios."
            );
        }

        // O bloqueio por leitor serializa empréstimos
        // concorrentes realizados pelo mesmo leitor.
        Leitor leitor = leitorRepository
            .buscarPorIdComBloqueio(leitorId)
            .orElseThrow(() -> new NoSuchElementException(
                "Leitor não encontrado: " + leitorId
            ));

        // O bloqueio por livro protege o estoque.
        Livro livro = livroRepository
            .buscarPorIdComBloqueio(livroId)
            .orElseThrow(() -> new NoSuchElementException(
                "Livro não encontrado: " + livroId
            ));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new IllegalArgumentException(
                "Não há exemplares disponíveis deste livro."
            );
        }

        long ativos =
            emprestimoRepository.countByLeitorIdAndSituacao(
                leitorId,
                SituacaoEmprestimo.ATIVO
            );

        if (ativos >= LIMITE_EMPRESTIMOS) {
            throw new IllegalArgumentException(
                "O leitor já possui três empréstimos ativos."
            );
        }

        LocalDate hoje = LocalDate.now();

        Emprestimo emprestimo = new Emprestimo(
            livro,
            leitor,
            hoje,
            hoje.plusDays(PRAZO_DIAS)
        );

        livro.setQuantidadeDisponivel(
            livro.getQuantidadeDisponivel() - 1
        );

        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);

        return EmprestimoResponse.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarResponse() {
        return emprestimoRepository.findAll()
            .stream()
            .map(EmprestimoResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscarResponse(Long id) {
        Emprestimo emprestimo =
            emprestimoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                    "Empréstimo não encontrado: " + id
                ));

        return EmprestimoResponse.fromEntity(emprestimo);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarPorLeitorResponse(
            Long leitorId) {

        return emprestimoRepository.findByLeitorId(leitorId)
            .stream()
            .map(EmprestimoResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarPorLivroResponse(
            Long livroId) {

        return emprestimoRepository.findByLivroId(livroId)
            .stream()
            .map(EmprestimoResponse::fromEntity)
            .toList();
    }

    @Transactional
    public EmprestimoResponse devolverResponse(Long emprestimoId) {

        if (emprestimoId == null) {
            throw new IllegalArgumentException(
                "O ID do empréstimo é obrigatório."
            );
        }

        Emprestimo emprestimo = emprestimoRepository
    .buscarPorIdComBloqueio(emprestimoId)
            .orElseThrow(() -> new NoSuchElementException(
                "Empréstimo não encontrado: " + emprestimoId
            ));

        // O bloqueio do livro serializa as alterações
        // na quantidade de exemplares disponíveis.
        Livro livro = livroRepository
            .buscarPorIdComBloqueio(emprestimo.getLivro().getId())
            .orElseThrow(() -> new NoSuchElementException(
                "Livro do empréstimo não encontrado."
            ));

        // Recarrega o empréstimo após obter o bloqueio.
        // É necessário validar concorrência de devoluções
        // com testes de integração.
        if (emprestimo.getSituacao()
                == SituacaoEmprestimo.DEVOLVIDO) {
            throw new IllegalArgumentException(
                "Este empréstimo já foi devolvido."
            );
        }

        emprestimo.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now());

        livro.setQuantidadeDisponivel(
            livro.getQuantidadeDisponivel() + 1
        );

        livroRepository.save(livro);

        Emprestimo salvo = emprestimoRepository.save(emprestimo);

        return EmprestimoResponse.fromEntity(salvo);
    }
}
