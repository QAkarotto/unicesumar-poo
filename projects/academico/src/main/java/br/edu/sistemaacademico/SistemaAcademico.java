package br.edu.sistemaacademico;

public class SistemaAcademico {

    public static void main(String[] args) {

        Aluno aluno = new Aluno(
                "A001",
                "Daniel",
                "daniel@email.com"
        );

        Disciplina disciplina = new Disciplina(
                "POO",
                "Programação Orientada a Objetos",
                80
        );

        PeriodoLetivo periodo = new PeriodoLetivo(
                2026,
                Semestre.PRIMEIRO
        );

        Turma turma = new Turma(
                "T001",
                disciplina,
                periodo
        );

        Matricula matricula = new Matricula(
                "M001",
                aluno,
                turma
        );

        System.out.println(aluno);
        System.out.println(disciplina);
        System.out.println(periodo);
        System.out.println(turma);
        System.out.println(matricula);
    }
}


// ==========================
// ALUNO
// ==========================

class Aluno {

    private final String identificadorAcademico;
    private String nome;
    private String email;

    public Aluno(String identificadorAcademico, String nome, String email) {

        if (identificadorAcademico == null ||
                identificadorAcademico.isBlank()) {

            throw new IllegalArgumentException(
                    "Identificador acadêmico é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome é obrigatório."
            );
        }

        validarEmail(email);

        this.identificadorAcademico = identificadorAcademico;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificadorAcademico() {
        return identificadorAcademico;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void alterarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome não pode ser vazio."
            );
        }

        this.nome = nome;
    }

    public void alterarEmail(String email) {

        validarEmail(email);
        this.email = email;
    }

    private void validarEmail(String email) {

        if (email == null ||
                email.isBlank() ||
                !email.contains("@")) {

            throw new IllegalArgumentException(
                    "E-mail inválido."
            );
        }
    }

    @Override
    public String toString() {

        return "Aluno: " +
                identificadorAcademico +
                " - " +
                nome +
                " - " +
                email;
    }
}


// ==========================
// DISCIPLINA
// ==========================

class Disciplina {

    private final String codigo;
    private String nome;
    private int cargaHoraria;

    public Disciplina(
            String codigo,
            String nome,
            int cargaHoraria) {

        if (codigo == null || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "Código é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome é obrigatório."
            );
        }

        if (cargaHoraria <= 0) {

            throw new IllegalArgumentException(
                    "Carga horária deve ser positiva."
            );
        }

        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {

        return "Disciplina: " +
                codigo +
                " - " +
                nome +
                " - " +
                cargaHoraria +
                " horas";
    }
}


// ==========================
// SEMESTRE
// ==========================

enum Semestre {
    PRIMEIRO,
    SEGUNDO
}


// ==========================
// PERÍODO LETIVO
// ==========================

class PeriodoLetivo {

    private final int ano;
    private final Semestre semestre;

    public PeriodoLetivo(int ano, Semestre semestre) {

        if (ano <= 0) {

            throw new IllegalArgumentException(
                    "Ano inválido."
            );
        }

        if (semestre == null) {

            throw new IllegalArgumentException(
                    "Semestre é obrigatório."
            );
        }

        this.ano = ano;
        this.semestre = semestre;
    }

    public int getAno() {
        return ano;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    @Override
    public String toString() {

        return "Período: " +
                ano +
                " - " +
                semestre;
    }
}


// ==========================
// TURMA
// ==========================

class Turma {

    private final String codigo;
    private final Disciplina disciplina;
    private final PeriodoLetivo periodoLetivo;

    public Turma(
            String codigo,
            Disciplina disciplina,
            PeriodoLetivo periodoLetivo) {

        if (codigo == null || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "Código da turma é obrigatório."
            );
        }

        if (disciplina == null) {

            throw new IllegalArgumentException(
                    "Disciplina é obrigatória."
            );
        }

        if (periodoLetivo == null) {

            throw new IllegalArgumentException(
                    "Período letivo é obrigatório."
            );
        }

        this.codigo = codigo;
        this.disciplina = disciplina;
        this.periodoLetivo = periodoLetivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    @Override
    public String toString() {

        return "Turma: " +
                codigo +
                " - " +
                disciplina.getNome() +
                " - " +
                periodoLetivo;
    }
}


// ==========================
// MATRÍCULA
// ==========================

class Matricula {

    private final String codigo;
    private final Aluno aluno;
    private final Turma turma;

    public Matricula(
            String codigo,
            Aluno aluno,
            Turma turma) {

        if (codigo == null || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "Código da matrícula é obrigatório."
            );
        }

        if (aluno == null) {

            throw new IllegalArgumentException(
                    "Aluno é obrigatório."
            );
        }
        if (turma == null) {

            throw new IllegalArgumentException(
                    "Turma é obrigatória."
            );
        }
        this.codigo = codigo;
        this.aluno = aluno;
        this.turma = turma;
    }
    public String getCodigo() {
        return codigo;
    }
    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }
    @Override
    public String toString() {

        return "Matrícula: " +
                codigo +
                " - Aluno: " +
                aluno.getNome() +
                " - Turma: " +
                turma.getCodigo();
    }
}
//Leonardo Havrechaki da Silva RA253639052,Daniel Camilo Rickli
//240551802//
