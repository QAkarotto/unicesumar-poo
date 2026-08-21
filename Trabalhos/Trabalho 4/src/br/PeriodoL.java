package br;

public class PeriodoL {
    private int ano;
    private int semestreL;
    public PeriodoL(int ano,int semestreL){
        this.ano=ano;
        this.semestreL=semestreL;
    }
    public int getAno(){
        return ano;
    }
    public void setAno(int ano){
        this.ano=ano;
    }
    public int getPeriodoL(){
        return semestreL;
    }
    public void setPeriodoL(int semestreL){
        this.semestreL=semestreL;
    }
    public void apresentaPeriodo(){
        System.out.println("Ano"+ano);
        System.out.println("Periodo letivo"+semestreL);
    }
}
