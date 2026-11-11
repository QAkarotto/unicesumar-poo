package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.exception.RecursoNaoEncontradoException;
import br.edu.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroService {

    private final LivroRepository livros;

    public LivroService(LivroRepository livros) {
        this.livros = livros;
    }

    @Transactional
    public Livro cadastrar(String titulo, String autor, String isbn) {
        Livro livro = new Livro(titulo, autor, isbn);
        if (livros.existsByIsbn(livro.getIsbn())) {
            throw new IllegalStateException("ISBN já cadastrado: " + livro.getIsbn());
        }
        return livros.save(livro);
    }

    @Transactional(readOnly = true)
    public Livro buscar(Long id) {
        return livros.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
    }
}