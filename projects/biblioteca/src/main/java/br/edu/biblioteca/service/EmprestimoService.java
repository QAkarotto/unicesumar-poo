package br.edu.biblioteca.service;

import br.edu.biblioteca.domain.Emprestimo;
import br.edu.biblioteca.domain.Livro;
import br.edu.biblioteca.domain.SituacaoEmprestimo;
import br.edu.biblioteca.domain.Usuario;
import br.edu.biblioteca.exception.RecursoNaoEncontradoException;
import br.edu.biblioteca.repository.EmprestimoRepository;
import br.edu.biblioteca.repository.LivroRepository;
import br.edu.biblioteca.repository.UsuarioRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimos;
    private final UsuarioRepository usuarios;
    private final LivroRepository livros;

    public EmprestimoService(EmprestimoRepository emprestimos,
                             UsuarioRepository usuarios,
                             LivroRepository livros) {
        this.emprestimos = emprestimos;
        this.usuarios = usuarios;
        this.livros = livros;
    }

    @Transactional
    public Emprestimo emprestar(Long usuarioId, Long livroId) {
        if (usuarioId == null || livroId == null) {
            throw new IllegalArgumentException("usuarioId e livroId são obrigatórios.");
        }

        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + usuarioId));

        Livro livro = livros.findById(livroId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + livroId));

        long ativosDoUsuario = emprestimos.countByUsuarioIdAndSituacao(usuarioId, SituacaoEmprestimo.ATIVO);
        boolean livroEmprestado = emprestimos.existsByLivroIdAndSituacao(livroId, SituacaoEmprestimo.ATIVO);

        Emprestimo.validarNovo(ativosDoUsuario, livroEmprestado);

        return emprestimos.save(new Emprestimo(usuario, livro, LocalDate.now()));
    }

    @Transactional
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = buscar(id);
        emprestimo.devolver(LocalDate.now());
        return emprestimo;
    }

    @Transactional(readOnly = true)
    public Emprestimo buscar(Long id) {
        return emprestimos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado: " + id));
    }
}