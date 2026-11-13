package br.edu.controleestoque.service;

import br.edu.controleestoque.domain.Fornecedor;
import br.edu.controleestoque.api.FornecedorRequest;
import br.edu.controleestoque.api.FornecedorResponse;
import br.edu.controleestoque.domain.OperacaoNaoPermitidaException;
import br.edu.controleestoque.domain.RecursoNaoEncontradoException;
import br.edu.controleestoque.repository.FornecedorRepository;
import br.edu.controleestoque.repository.MovimentacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;
    private final MovimentacaoRepository movimentacaoRepository;

    public FornecedorService(FornecedorRepository repository, MovimentacaoRepository movimentacaoRepository) {
        this.repository = repository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponse> listar() {
        return repository.findAllByOrderByNomeAsc().stream().map(FornecedorResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscar(Long id) {
        return FornecedorResponse.de(obter(id));
    }

    @Transactional
    public FornecedorResponse criar(FornecedorRequest req) {
        String cnpj = Fornecedor.normalizarCnpj(req.cnpj());
        if (repository.existsByCnpj(cnpj)) {
            throw new OperacaoNaoPermitidaException("Já existe um fornecedor com o CNPJ " + cnpj + ".");
        }
        var fornecedor = new Fornecedor(req.nome(), cnpj, req.email(), req.telefone());
        return FornecedorResponse.de(repository.save(fornecedor));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest req) {
        var fornecedor = obter(id);
        String cnpj = Fornecedor.normalizarCnpj(req.cnpj());
        if (repository.existsByCnpjAndIdNot(cnpj, id)) {
            throw new OperacaoNaoPermitidaException("Já existe um fornecedor com o CNPJ " + cnpj + ".");
        }
        fornecedor.alterarDados(req.nome(), cnpj, req.email(), req.telefone());
        return FornecedorResponse.de(fornecedor);
    }

    /** Regra: fornecedor que já realizou entradas não pode ser excluído (histórico). */
    @Transactional
    public void excluir(Long id) {
        var fornecedor = obter(id);
        if (movimentacaoRepository.existsByFornecedorId(id)) {
            throw new OperacaoNaoPermitidaException(
                    "O fornecedor possui entradas registradas no histórico e não pode ser excluído.");
        }
        fornecedor.desvincularDeTodosOsProdutos();
        repository.delete(fornecedor);
    }

    Fornecedor obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor", id));
    }
}
