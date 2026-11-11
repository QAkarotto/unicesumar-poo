package br.edu.sistemaacademico.service;

import br.edu.sistemaacademico.domain.Aluno;
import br.edu.sistemaacademico.domain.Matricula;
import br.edu.sistemaacademico.domain.OfertaDisciplina;
import br.edu.sistemaacademico.dto.ConcluirMatriculaRequest;
import br.edu.sistemaacademico.dto.CriarMatriculaRequest;
import br.edu.sistemaacademico.dto.MatriculaResponse;
import br.edu.sistemaacademico.exception.RecursoNaoEncontradoException;
import br.edu.sistemaacademico.exception.RegraDeNegocioException;
import br.edu.sistemaacademico.repository.AlunoRepository;
import br.edu.sistemaacademico.repository.MatriculaRepository;
import br.edu.sistemaacademico.repository.OfertaDisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final OfertaDisciplinaRepository ofertaRepository;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            OfertaDisciplinaRepository ofertaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.ofertaRepository = ofertaRepository;
    }

    @Transactional
    public MatriculaResponse matricular(CriarMatriculaRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Corpo da requisição é obrigatório.");
        }
        String ra = request.obterRegistroAcademico();
        String ofertaIdStr = request.obterOfertaIdentificador();

        if (ra == null || ra.isBlank() || ofertaIdStr == null || ofertaIdStr.isBlank()) {
            throw new IllegalArgumentException("Informe registroAcademico e ofertaId.");
        }

        Aluno aluno = alunoRepository.findByRegistroAcademico(ra.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado."));

        OfertaDisciplina oferta = buscarOferta(ofertaIdStr);

        // Regra 1: Aluno não pode se matricular em disciplina que já foi aprovado
        List<Matricula> matriculasAluno = matriculaRepository.findByAlunoRegistroAcademico(aluno.getRegistroAcademico());
        for (Matricula m : matriculasAluno) {
            if (m.foiAprovadoEm(oferta.getDisciplina())) {
                throw new IllegalStateException("O aluno já foi aprovado nesta disciplina.");
            }
        }

        // Regra 2: Aluno não pode estar matriculado duas vezes na mesma oferta
        boolean jaMatriculado = matriculaRepository.existsByAlunoRegistroAcademicoAndOfertaDisciplinaCodigo(
                aluno.getRegistroAcademico(), oferta.getCodigo());
        if (jaMatriculado) {
            throw new IllegalArgumentException("O aluno já está matriculado nesta oferta.");
        }

        // Validação no próprio domínio para manter integridade das regras
        oferta.validarNovaMatricula(aluno);
        aluno.validarNovaMatricula(oferta);

        long totalMatriculas = matriculaRepository.count();
        String codigoMatricula = String.format("MAT-%03d", totalMatriculas + 1);

        // Criação e vinculação no domínio
        var matricula = new Matricula(codigoMatricula, aluno, oferta);
        var salva = matriculaRepository.save(matricula);

        return MatriculaResponse.de(salva);
    }

    @Transactional(readOnly = true)
    public MatriculaResponse buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código de matrícula é obrigatório.");
        }
        return matriculaRepository.findByCodigo(codigo.trim())
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));
    }

    @Transactional(readOnly = true)
    public MatriculaResponse buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .map(MatriculaResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponse> listarTodas() {
        return matriculaRepository.findAll().stream()
                .map(MatriculaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponse> listarPorAluno(String ra) {
        return matriculaRepository.findByAlunoRegistroAcademico(ra).stream()
                .map(MatriculaResponse::de)
                .toList();
    }

    @Transactional
    public MatriculaResponse concluir(Long id, ConcluirMatriculaRequest request) {
        if (request == null || request.resultado() == null) {
            throw new IllegalArgumentException("O resultado acadêmico é obrigatório.");
        }
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));

        // Regra de negócio no domínio: exige situação ATIVA e atualiza resultado
        matricula.concluir(request.resultado());
        var atualizada = matriculaRepository.save(matricula);
        return MatriculaResponse.de(atualizada);
    }

    @Transactional
    public MatriculaResponse cancelar(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));

        matricula.cancelar();
        var atualizada = matriculaRepository.save(matricula);
        return MatriculaResponse.de(atualizada);
    }

    @Transactional
    public MatriculaResponse trancar(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));

        matricula.trancar();
        var atualizada = matriculaRepository.save(matricula);
        return MatriculaResponse.de(atualizada);
    }

    @Transactional
    public void excluir(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matrícula não encontrada."));

        if (matricula.getAluno() != null) {
            matricula.getAluno().removerMatricula(matricula);
        }
        if (matricula.getOfertaDisciplina() != null) {
            matricula.getOfertaDisciplina().removerMatricula(matricula);
        }
        matriculaRepository.delete(matricula);
        matriculaRepository.flush();
    }

    private OfertaDisciplina buscarOferta(String identificador) {
        try {
            Long id = Long.parseLong(identificador);
            return ofertaRepository.findById(id)
                    .orElseGet(() -> ofertaRepository.findByCodigo(identificador)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta não encontrada.")));
        } catch (NumberFormatException e) {
            return ofertaRepository.findByCodigo(identificador)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta não encontrada."));
        }
    }
}
