package br.edu.sistemaacademico;

public class SistemaAcademico {

    public static void main(String[] args) {

        Aluno aluno = new Aluno("2508333-2", "andrey kayky baitler", "andreybaitler212@gmail.com");

        Disciplina disciplina = new Disciplina(
                "JAVA01",
                "Programacao Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(2026, Semestre.PRIMEIRO);

        Turma turma = new Turma("NA", disciplina, periodo);

        Matricula matricula = new Matricula("MAT500", aluno, turma);

        System.out.println("--SISTEMA ACADEMICO--");
        System.out.println();
        System.out.println(aluno);
        System.out.println(disciplina);
        System.out.println(periodo);
        System.out.println(turma);
        System.out.println(matricula);
    }
}

//ALUNO/
class Aluno {

    private final String ra;
    private String nomeCompleto;
    private String email;

    public Aluno(String ra, String nomeCompleto, String email) {

        if (textoInvalido(ra)) {
            throw new IllegalArgumentException("ra nao informado");
        }
        definirNome(nomeCompleto);
        trocarEmail(email);
        this.ra = ra;
    }

    private boolean textoInvalido(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void definirNome(String nome) {

        if (textoInvalido(nome)) {
            throw new IllegalArgumentException("nome invalido");
        }

        this.nomeCompleto = nome.trim();
    }

    private void validarEmail(String endereco) {

        if (textoInvalido(endereco)
                || !endereco.contains("@")
                || !endereco.contains(".")) {

            throw new IllegalArgumentException("email invalido");
        }
    }

    public void trocarEmail(String novoEmail) {

        validarEmail(novoEmail);
        this.email = novoEmail.trim();
    }

    public void atualizarNome(String novoNome) {
        definirNome(novoNome);
    }

    public String getRa() {
        return ra;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Aluno: " + nomeCompleto
                + " | RA: " + ra
                + " | Email: " + email;
    }
}


// DISCIPLINA//

class Disciplina {

    private final String codigo;
    private String descricao;
    private int horas;

    public Disciplina(String codigo, String descricao, int horas) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo invalido");
        }

        atualizarDescricao(descricao);
        definirCargaHoraria(horas);

        this.codigo = codigo.trim();
    }

    public void atualizarDescricao(String novaDescricao) {

        if (novaDescricao == null || novaDescricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "descricao da disciplina obrigatoria"
            );
        }

        this.descricao = novaDescricao.trim();
    }

    public void definirCargaHoraria(int quantidade) {

        if (quantidade < 1) {
            throw new IllegalArgumentException(
                    "acarga horaria deve ser maior que zero"
            );
        }

        this.horas = quantidade;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getHoras() {
        return horas;
    }

    @Override
    public String toString() {
        return "Disciplina: " + codigo
                + " | " + descricao
                + " | Carga horaria: " + horas + "h";
    }
}



// SEMESTRE/

enum Semestre {
    PRIMEIRO,
    SEGUNDO
}


// PERIODO LETIVO

class PeriodoLetivo {

    private final int ano;
    private final Semestre etapa;

    public PeriodoLetivo(int ano, Semestre etapa) {

        if (ano < 2000) {
            throw new IllegalArgumentException(
                    "ano letivo invalido."
            );
        }

        if (etapa == null) {
            throw new IllegalArgumentException(
                    "O semestre precisa ser informado."
            );
        }

        this.ano = ano;
        this.etapa = etapa;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getEtapa() {
        return etapa;
    }

    @Override
    public String toString() {
        return "Periodo: " + ano
                + " | Semestre: " + etapa;
    }
}

// TURMA


class Turma {

    private final String identificacao;
    private final Disciplina materia;
    private final PeriodoLetivo periodo;

    public Turma(
            String identificacao,
            Disciplina materia,
            PeriodoLetivo periodo) {

        if (identificacao == null || identificacao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Identificacao da turma obrigatoria."
            );
        }

        if (materia == null) {
            throw new IllegalArgumentException(
                    "A turma precisa possuir uma disciplina."
            );
        }

        if (periodo == null) {
            throw new IllegalArgumentException(
                    "A turma precisa possuir um periodo."
            );
        }

        this.identificacao = identificacao.trim();
        this.materia = materia;
        this.periodo = periodo;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public Disciplina getMateria() {
        return materia;
    }

    public PeriodoLetivo getPeriodo() {
        return periodo;
    }

    @Override
    public String toString() {
        return "Turma: " + identificacao
                + " | Disciplina: " + materia.getDescricao()
                + " | Ano: " + periodo.getAno()
                + " | Semestre: " + periodo.getEtapa();
    }
}



// MATRICULA

class Matricula {

    private final String numero;
    private final Aluno estudante;
    private final Turma classe;

    public Matricula(
            String numero,
            Aluno estudante,
            Turma classe) {

        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Numero da matricula obrigatorio."
            );
        }

        if (estudante == null) {
            throw new IllegalArgumentException(
                    "Aluno nao pode ser nulo."
            );
        }

        if (classe == null) {
            throw new IllegalArgumentException(
                    "Turma nao pode ser nula."
            );
        }

        this.numero = numero.trim();
        this.estudante = estudante;
        this.classe = classe;
    }

    public String getNumero() {
        return numero;
    }

    public Aluno getEstudante() {
        return estudante;
    }

    public Turma getClasse() {
        return classe;
    }

    @Override
    public String toString() {
        return "Matricula: " + numero
                + " | Aluno: " + estudante.getNomeCompleto()
                + " | Turma: " + classe.getIdentificacao();
    }
}
////////////andrey kayky baitler ra-2508333-2////////////
////////////Gregory Jonker de Macedo ra-25361553-2////////////