package br;

import java.util.Scanner;
public class Main {
         public static void main(String[] args) {
             Scanner sc = new Scanner(System.in);

             Aluno aluno1 = new Aluno("Goku", 8001, "Gk@uni");
             Turma turma1 = new Turma("POO", 1);
             int opcao;

                 do {

                     System.out.println("Escolha uma opção:");
                     System.out.println("1 - Mostrar Aluno");
                     System.out.println("2 - Mostrar Tumar");
                     System.out.println("3 - Mostrar Diciplina");
                     System.out.println("4 - Mostrar Matricula");
                     System.out.println("5 - Mostrar Periodo");
                     System.out.println("6 - Mostrar Semestre");
                     System.out.println("Sair aperte0");

                     opcao = sc.nextInt();
                     switch (opcao) {

                         case 1:
                             System.out.println("Aluno: " + aluno1.getNome() + " (Id: " + aluno1.getId() + "Email: " + aluno1.getEmail());

                             break;
                         case 2:
                             System.out.println("Turma1: " + turma1.getMateria() + "  " + turma1.getPeriodo());
                             break;
                         case 3:
                             Diciplina d1 = new Diciplina("POO", 60);
                             d1.apresentarDiciplina();

                             break;
                         case 4:
                             Matricula m1 = new Matricula(aluno1, turma1);
                             m1.apresentarMatricula();
                             break;
                         case 5:
                             PeriodoL p0 = new PeriodoL(2026, 4);
                             p0.apresentaPeriodo();
                             break;
                         case 6:
                             Semes semestre = new Semes(true);

                             if (semestre.getAceito()) {
                                 System.out.println(" semestre foi aceito!");
                             } else {
                                 System.out.println("semestre não foi aceito.");
                             }
                             break;
                         case 7:
                             System.out.println("Programa finalizado!");
                             break;
                         default:
                             System.out.println("Opção inválida!");
                     }
                 } while (opcao != 0);

                 sc.close();
             }
             }

