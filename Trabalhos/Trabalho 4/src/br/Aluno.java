package br;

public class Aluno {
    public String nome;
    public int id;
    public String email;
    public Aluno(String nome,int id,String email){
        this.nome=nome;
        this.id=id;
        this.email=email;
    }
    public  String getNome(){
        return nome;
    }
    public void setNome( String nome){
        this.nome=nome;
    }
    public  int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
      this.email=email;
    }
    public void apresentaAluno(){
        System.out.println("O nome do Aluno: "+nome);
        System.out.println("O número do Id: "+id);
        System.out.println("O Email: "+email);

    }
}
