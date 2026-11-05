package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.dto.ConcluirMatriculaRequest;
import br.edu.sistemaacademico.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.dto.MatriculaResponse;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.MatriculaRepository;
import br.edu.sistemaacademico.repository.OfertaDisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MatriculaService {

    private final AlunoRepository alunoRepository;
    private final OfertaDisciplinaRepository ofertaRepository;
    private final MatriculaRepository matriculaRepository;

    public MatriculaService(
            AlunoRepository alunoRepository,
            OfertaDisciplinaRepository ofertaRepository,
            MatriculaRepository matriculaRepository) {
        this.alunoRepository = alunoRepository;
        this.ofertaRepository = ofertaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public MatriculaResponse matricular(CriarMatriculaRequest request) {
        if (request.raAluno() == null || request.idOferta() == null) {
            throw new IllegalArgumentException("Aluno e oferta são obrigatórios.");
        }

        Aluno aluno = alunoRepository.findById(request.raAluno())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado: " + request.raAluno()));

        OfertaDisciplina oferta = ofertaRepository.findById(request.idOferta())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta não encontrada: " + request.idOferta()));

        Matricula matricula = oferta.matricular(aluno);
        return MatriculaResponse.de(matriculaRepository.save(matricula));
    }

    public List<MatriculaResponse> listar() {
        return matriculaRepository.findAll().stream().map(MatriculaResponse::de).toList();
    }

    public MatriculaResponse buscarPorId(Long id) {
        return MatriculaResponse.de(buscar(id));
    }

    public MatriculaResponse concluir(Long id, ConcluirMatriculaRequest request) {
        Matricula matricula = buscar(id);
        matricula.concluir(request.resultado());
        return MatriculaResponse.de(matriculaRepository.save(matricula));
    }

    private Matricula buscar(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada: " + id));
    }
}
