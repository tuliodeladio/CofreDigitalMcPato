package view.menu;

import model.Account;
import model.AccountEmpresa;
import model.AccountPessoaFisica;
import service.AuthService;
import validator.UserValidator;
import validator.ValidationException;

import java.util.Random;
import java.util.Scanner;

public class Register {
    public static void handleMenu(Scanner sc) {
        AuthService authService = new AuthService();

        try {
            System.out.print("Pessoa Física ou Empresa (F/E)? ");
            String tipo = sc.nextLine().toUpperCase();

            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Senha: ");
            String senha = sc.nextLine();

            UserValidator.validateEmail(email);
            UserValidator.validatePassword(senha);

            String hash = AuthService.hashPassword(senha);
            Random rand = new Random();
            String contaNum = String.format("%04d", rand.nextInt(10000));

            Account acc;
            if ("E".equals(tipo)) {
                System.out.print("CNPJ: ");
                String cnpj = sc.nextLine();
                UserValidator.validateCnpj(cnpj);
                acc = new AccountEmpresa(contaNum, nome, email, hash, cnpj, 0.0);
            } else {
                System.out.print("CPF: ");
                String cpf = sc.nextLine();
                UserValidator.validateCpf(cpf);
                acc = new AccountPessoaFisica(contaNum, nome, email, hash, cpf, 0.0);
            }

            authService.register(acc);
            System.out.println("Conta registrada! Número: " + acc.getAccountNumber());
        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        }
    }
}
