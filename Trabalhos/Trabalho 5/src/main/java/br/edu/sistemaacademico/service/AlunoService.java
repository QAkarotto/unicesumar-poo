package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.dto.AlunoResponse;
import br.edu.sistemaacademico.dto.CriarAlunoRequest;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegistroDuplicadoException;
import br.edu.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoResponse cadastrar(CriarAlunoRequest request) {
        Aluno aluno = new Aluno(request.ra(), request.nome(), request.email());

        if (alunoRepository.existsById(aluno.getRa())) {
            throw new RegistroDuplicadoException("Já existe aluno com o RA: " + aluno.getRa());
        }
        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new RegistroDuplicadoException("Já existe aluno com o e-mail: " + aluno.getEmail());
        }

        return AlunoResponse.de(alunoRepository.save(aluno));
    }

    public List<AlunoResponse> listar() {
        return alunoRepository.findAll().stream().map(AlunoResponse::de).toList();
    }

    public AlunoResponse buscarPorRa(String ra) {
        return alunoRepository.findById(ra)
                .map(AlunoResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado: " + ra));
    }
}
