package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.dto.CriarAlunoRequest;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegraDeNegocioException;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public AlunoResponse cadastrar(CriarAlunoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        String ra = request.obterRa();
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("O registro acadêmico é obrigatório.");
        }
        if (alunoRepository.existsByRegistroAcademico(ra)) {
            throw new RegraDeNegocioException("Já existe um aluno cadastrado com o registro acadêmico informado.");
        }

        var aluno = new Aluno(ra, request.nome(), request.email());
        var salvo = alunoRepository.save(aluno);
        return AlunoResponse.de(salvo);
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorRa(String ra) {
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("O registro acadêmico é obrigatório.");
        }
        return alunoRepository.findByRegistroAcademico(ra.trim())
                .map(AlunoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(AlunoResponse::de)
                .toList();
    }
}
