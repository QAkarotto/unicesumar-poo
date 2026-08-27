package br.edu.sistemaacademico.domain;
import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String codigo;
    private final List<OfertaDisciplina> ofertas = new ArrayList<>();
    private PeriodoLetivo periodoLetivo;

    public Turma(String codigo, PeriodoLetivo periodoLetivo) {
        if (codigo == null || codigo.equals("")) {
            throw new IllegalArgumentException("O código da turma é obrigatório.");
        }
        if (periodoLetivo == null) {
            throw new IllegalArgumentException("O período letivo é obrigatório.");
        }
        this.codigo = codigo;
        this.periodoLetivo = periodoLetivo;
    }

    public OfertaDisciplina ofertarDisciplina(Disciplina disciplina){
        if (disciplina == null){
            throw new IllegalArgumentException("A disciplina é obrigatória.");
        }
        for (OfertaDisciplina oferta: this.ofertas){
            if (oferta.getDisciplina().getCodigo().equals(disciplina.getCodigo())){
                throw new IllegalArgumentException("A disciplina "+disciplina.getCodigo()+" já foi passada nesta turma.");
            }
        }
        OfertaDisciplina oferta = new OfertaDisciplina(this, disciplina);
        this.ofertas.add(oferta);
        return oferta;
    }
    public String getCodigo() {
        return this.codigo;
    }
    public List<OfertaDisciplina> getOfertas() {
        return this.ofertas;
    }
    public PeriodoLetivo getPeriodoLetivo() {
        return this.periodoLetivo;
    }

    @Override
    public String toString() {
        return "Turma " + this.codigo + " [" + this.periodoLetivo + "]";
    }
}