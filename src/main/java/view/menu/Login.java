package view.menu;

import model.Account;
import service.AuthService;
import service.TwoFactorService;
import validator.UserValidator;
import validator.ValidationException;

import java.util.Scanner;

public class Login {
    public static Account menuHandler(Account currentUser, Scanner sc) {
        AuthService authService = new AuthService();
        TwoFactorService twoFactorService = new TwoFactorService();

        try {
            System.out.print("Email: ");
            String loginEmail = sc.nextLine();

            System.out.print("Senha: ");
            String loginSenha = sc.nextLine();

            UserValidator.validateEmail(loginEmail);
            UserValidator.validatePassword(loginSenha);

            Account loginAcc = authService.login(loginEmail, loginSenha);
            if (loginAcc != null) {
                twoFactorService.generateAndSendCode(loginEmail);
                System.out.print("Digite o código 2FA enviado: ");
                String inputCode = sc.nextLine();

                if (twoFactorService.validateCode(inputCode)) {
                    currentUser = loginAcc;
                    System.out.println("Login realizado com sucesso!");
                } else {
                    System.out.println("2FA incorreto!");
                }
            } else {
                System.out.println("Credenciais inválidas!");
            }

        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        }

        return currentUser;
    }
}
