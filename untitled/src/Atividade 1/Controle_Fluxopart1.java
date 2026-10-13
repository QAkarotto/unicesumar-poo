import java.util.Scanner;

public class Controle_Fluxopart1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println ("------- Atividade 2: Controle de Fluxo -------");
        System.out.println ("---- Clássico (if/else) ----");
        System.out.print("Digite a média final do aluno: ");
        var media = scanner.nextDouble();

        System.out.print("Digite o total de faltas do aluno: ");
        var faltas = scanner.nextInt();

        String status;

        if (faltas > 20) {
            status = "Reprovado por falta!!!";
        } else if (media >= 6.0) {
            status = "Aprovado";
        } else {
            status = "Sub";
        }

        System.out.println("Status: " + status);

        scanner.close();
    }
}