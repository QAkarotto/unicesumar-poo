import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turma {

    private final String codigo;
    private final PeriodoLetivo periodo;

    private final List<OfertaDisciplina> ofertas;

    public Turma(
            String codigo,
            PeriodoLetivo periodo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Codigo da turma obrigatorio."
            );
        }

        if (periodo == null) {
            throw new IllegalArgumentException(
                    "Periodo letivo obrigatorio."
            );
        }

        this.codigo = codigo.trim();
        this.periodo = periodo;

        this.ofertas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public PeriodoLetivo getPeriodo() {
        return periodo;
    }

    /*
     * Retorna as disciplinas ofertadas pela turma.
     */
    public List<OfertaDisciplina> getOfertas() {
        return Collections.unmodifiableList(ofertas);
    }

    /*
     * Adiciona uma disciplina à turma.
     */
    public OfertaDisciplina ofertarDisciplina(
            Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "Disciplina nao pode ser nula."
            );
        }

        /*
         * Impede a mesma disciplina duas vezes
         * na mesma turma.
         */
        for (OfertaDisciplina oferta : ofertas) {

            if (oferta.getDisciplina().equals(disciplina)) {

                throw new IllegalArgumentException(
                        "Disciplina ja ofertada nesta turma."
                );
            }
        }

        OfertaDisciplina oferta =
                new OfertaDisciplina(
                        disciplina,
                        this
                );

        ofertas.add(oferta);

        return oferta;
    }

    @Override
    public String toString() {

        return "Turma: " + codigo
                + " | Periodo: " + periodo;
    }
}