import model.*;
import service.*;
import validator.*;
import view.menu.*;
import view.menu.Transfer;

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc;

        if (args.length > 0) {
            try {
                sc = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado: " + args[0]);
                return;
            }
        } else {
            sc = new Scanner(System.in);
        }

        Account currentUser = null;
        boolean isAuthenticated = false;

        try {
            while (true) {
                System.out.println("\n--- Cofre Digital McPato ---");
                System.out.println("1. Registrar");
                System.out.println("2. Login");
                System.out.println("3. Comprar/Vender Ativo");
                System.out.println("4. Transferência");
                System.out.println("5. Relatório");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");

                int op;
                try {
                    op = Integer.parseInt(sc.nextLine());
                } catch (NoSuchElementException nse) {
                    System.out.println("Fim da entrada.");
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida!");
                    continue;
                }

                if (op == 0) {
                    break;
                }

                switch (op) {
                    case 1:
                        // Registro PF/PJ
                        // Insere uma nova conta no banco de dados
                        Register.handleMenu(sc);
                        break;

                    case 2:
                        // Login + 2FA (mantido)
                        currentUser = Login.menuHandler(currentUser, sc);

                        if (currentUser != null) {
                            isAuthenticated = true;
                        }

                        break;

                    case 3:
                        // Compra/Venda
                        // Grava e lê registros de operações de compra e venda, e atualiza o saldo da conta no banco de dados)
                        if (isAuthenticated) {
                            BuyAndSell.menuHandler(currentUser, sc);
                        } else {
                            System.out.println("Necessário login!");
                        }

                        break;

                    case 4:
                        // Transferências
                        // Grava e lê os registros de transferencias, saques e depósitos, e atualiza o saldo da conta no banco de dados
                        if (isAuthenticated) {
                            Transfer.menuHandler(currentUser, sc);
                        } else {
                            System.out.println("Necessário login!");
                        }

                        break;

                    case 5:
                        // Relatório
                        // Lê do banco de dados registros de transferências, operações, lista de ativos, saldo, etc
                        if (isAuthenticated) {
                            Report.menuHandler(currentUser);
                        } else {
                            System.out.println("Necessário login!");
                        }

                        break;

                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } finally {
            sc.close();
        }

        System.out.println("Aplicação encerrada.");
    }
}
