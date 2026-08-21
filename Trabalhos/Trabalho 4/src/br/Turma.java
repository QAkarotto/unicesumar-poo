package br;

public class Turma {
    public String materia;
    public int periodo;

    public Turma(String materia, int periodo) {
        this.materia = materia;
        this.periodo = periodo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
