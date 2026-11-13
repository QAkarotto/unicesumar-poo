package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.Categoria;
import br.edu.controleestoque.api.CategoriaRequest;
import br.edu.controleestoque.api.CategoriaResponse;
import br.edu.controleestoque.domain.OperacaoNaoPermitidaException;
import br.edu.controleestoque.domain.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return repository.findAllByOrderByNomeAsc().stream().map(CategoriaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id) {
        return CategoriaResponse.de(obter(id));
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest req) {
        if (repository.existsByNomeIgnoreCase(req.nome().trim())) {
            throw new OperacaoNaoPermitidaException("Já existe uma categoria com o nome " + req.nome() + ".");
        }
        var categoria = new Categoria(req.nome(), req.descricao());
        return CategoriaResponse.de(repository.save(categoria));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest req) {
        var categoria = obter(id);
        if (repository.existsByNomeIgnoreCaseAndIdNot(req.nome().trim(), id)) {
            throw new OperacaoNaoPermitidaException("Já existe uma categoria com o nome " + req.nome() + ".");
        }
        categoria.alterarDados(req.nome(), req.descricao());
        // sem save(): a entidade está gerenciada e o Hibernate faz UPDATE no commit (dirty checking)
        return CategoriaResponse.de(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        var categoria = obter(id);
        if (categoria.possuiProdutos()) {
            throw new OperacaoNaoPermitidaException(
                    "A categoria possui produtos cadastrados e não pode ser excluída.");
        }
        repository.delete(categoria);
    }

    Categoria obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }
}
