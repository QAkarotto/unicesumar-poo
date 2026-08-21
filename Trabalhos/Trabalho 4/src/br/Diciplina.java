package br;

public class Diciplina {
    private String componenteC;
    private  int tempo;
    public Diciplina(String componenteC,int tempo){
        this.componenteC=componenteC;
        this.tempo=tempo;
    }
    public String getComponenteC(){
        return componenteC;
    }
    public void setComponenteC(String componenteC){
        this.componenteC=componenteC;
    }
    public int getTempo(){
        return tempo;
    }
    public void setTempo (int tempo){
        this.tempo=tempo;
    }
public void apresentarDiciplina(){
        System.out.println("Componente Curricular"+componenteC);
    System.out.println("Tempo Curricular"+tempo);
}
}
