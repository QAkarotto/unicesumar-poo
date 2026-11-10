package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Livro cadastrar(String isbn, String titulo, String autor, int quantidadeTotal) {
        isbn = isbn.trim();
        if (livroRepository.existsByIsbn(isbn)) {
            throw new IllegalStateException("Já existe um livro com este ISBN.");
        }
        return livroRepository.save(new Livro(isbn, titulo.trim(), autor.trim(), quantidadeTotal));
    }

    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado: " + id));
    }

    @Transactional
    public Livro atualizar(Long id, String titulo, String autor) {
        Livro livro = buscarPorId(id);
        livro.atualizarDados(titulo.trim(), autor.trim());
        return livro;
    }

    @Transactional
    public void excluir(Long id) {
        livroRepository.delete(buscarPorId(id));
    }
}
