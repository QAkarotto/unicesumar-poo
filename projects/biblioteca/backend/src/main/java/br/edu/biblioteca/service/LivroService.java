package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.dto.*;
import br.edu.biblioteca.exception.*;
import br.edu.biblioteca.repository.*;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LivroService {
    private final LivroRepository livros;
    private final EmprestimoRepository emprestimos;
    public LivroService(LivroRepository livros, EmprestimoRepository emprestimos) {
        this.livros = livros; this.emprestimos = emprestimos;
    }
    public List<LivroResponse> listar() { return livros.findAll(Sort.by("titulo")).stream().map(this::resposta).toList(); }
    public LivroResponse buscar(Long id) {
        return resposta(livros.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado.")));
    }
    @Transactional
    public LivroResponse criar(LivroRequest r) {
        if (livros.existsByIsbn(r.isbn())) throw new RegraNegocioException("Já existe um livro com este ISBN.");
        return resposta(livros.save(new Livro(r.titulo(), r.autor(), r.isbn(), r.categoria(), r.exemplares())));
    }
    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest r) {
        var livro = livros.buscarComBloqueio(id).orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado."));
        if (livros.existsByIsbnAndIdNot(r.isbn(), id)) throw new RegraNegocioException("Já existe um livro com este ISBN.");
        livro.atualizar(r.titulo(), r.autor(), r.isbn(), r.categoria(), r.exemplares(), emprestimos.countByLivroIdAndDevolvidoEmIsNull(id));
        return resposta(livro);
    }
    private LivroResponse resposta(Livro l) { return LivroResponse.de(l, emprestimos.countByLivroIdAndDevolvidoEmIsNull(l.getId())); }
}
