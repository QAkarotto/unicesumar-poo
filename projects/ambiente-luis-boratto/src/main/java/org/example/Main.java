package org.example;

public class Main {

    public static void main(String[] args) {
        System.out.println("Ola, mundo!");
        System.out.println();
        System.out.println("Ambiente de desenvolvimento configurado com sucesso.");
        System.out.println("Aluno......: Luis Gustavo Boratto (ADSIS4S)");
        System.out.println("Java.......: " + System.getProperty("java.version"));
        System.out.println("Fornecedor.: " + System.getProperty("java.vendor"));
        System.out.println("Sistema....: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
    }
}
