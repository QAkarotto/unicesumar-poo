
package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.repository.EmprestimoRepository;
import br.edu.biblioteca.repository.LivroRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LivroService(
            LivroRepository livroRepository,
            EmprestimoRepository emprestimoRepository) {

        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public Livro cadastrar(Livro livro) {

        validarLivro(livro);

        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new IllegalArgumentException(
                "Já existe um livro cadastrado com este ISBN."
            );
        }

        Livro novo = new Livro(
            livro.getTitulo(),
            livro.getAutor(),
            livro.getIsbn(),
            livro.getQuantidadeTotal(),
            livro.getQuantidadeTotal()
        );

        return livroRepository.save(novo);
    }

    @Transactional(readOnly = true)
    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Livro não encontrado: " + id
            ));
    }

    @Transactional
    public Livro atualizar(Long id, Livro dados) {

        validarLivro(dados);

        Livro livro = livroRepository
            .buscarPorIdComBloqueio(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Livro não encontrado: " + id
            ));

        if (!livro.getIsbn().equals(dados.getIsbn())
                && livroRepository.existsByIsbn(dados.getIsbn())) {

            throw new IllegalArgumentException(
                "Já existe outro livro com este ISBN."
            );
        }

        int emprestados = livro.getQuantidadeTotal()
                - livro.getQuantidadeDisponivel();

        if (dados.getQuantidadeTotal() < emprestados) {
            throw new IllegalArgumentException(
                "A quantidade total não pode ser menor "
                + "que o número de exemplares emprestados."
            );
        }

        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setIsbn(dados.getIsbn());

        livro.setQuantidadeTotal(dados.getQuantidadeTotal());
        livro.setQuantidadeDisponivel(
            dados.getQuantidadeTotal() - emprestados
        );

        return livroRepository.save(livro);
    }

    @Transactional
    public void excluir(Long id) {

        Livro livro = livroRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Livro não encontrado: " + id
            ));

        if (emprestimoRepository.existsByLivroId(id)) {
            throw new IllegalArgumentException(
                "Não é possível excluir um livro "
                + "que possui histórico de empréstimos."
            );
        }

        livroRepository.delete(livro);
    }

    private void validarLivro(Livro livro) {

        if (livro == null) {
            throw new IllegalArgumentException(
                "Os dados do livro são obrigatórios."
            );
        }

        if (livro.getTitulo() == null
                || livro.getTitulo().isBlank()
                || livro.getTitulo().length() > 200) {

            throw new IllegalArgumentException(
                "O título deve ter entre 1 e 200 caracteres."
            );
        }

        if (livro.getAutor() == null
                || livro.getAutor().isBlank()
                || livro.getAutor().length() > 150) {

            throw new IllegalArgumentException(
                "O autor deve ter entre 1 e 150 caracteres."
            );
        }

        if (livro.getIsbn() == null
                || livro.getIsbn().isBlank()
                || livro.getIsbn().length() > 20) {

            throw new IllegalArgumentException(
                "O ISBN deve ter entre 1 e 20 caracteres."
            );
        }

        if (livro.getQuantidadeTotal() == null
                || livro.getQuantidadeTotal() < 0) {

            throw new IllegalArgumentException(
                "A quantidade total não pode ser negativa."
            );
        }
    }
}
