package br;

public class Matricula {
    private Aluno aluno;
    private Turma turma;
    public Matricula(Aluno aluno,Turma turma){
        this.aluno=aluno;
        this.turma=turma;
    }
    public void apresentarMatricula(){
        System.out.println("Aluno:"+ aluno.getNome()+("id:"+ aluno.getId()+ "email:" + aluno.getEmail()));
        System.out.println(" Materia:"+ turma.getMateria()+("periodo:"+ turma.getPeriodo()));
    }
}