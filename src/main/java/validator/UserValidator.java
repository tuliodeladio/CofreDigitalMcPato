package validator;

import java.util.regex.Pattern;

public class UserValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$|^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{14}$|^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static void validateCpf(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            throw new ValidationException("CPF inválido! Informe somente números ou no formato 000.000.000-00.");
        }
    }

    public static void validateCnpj(String cnpj) {
        if (cnpj == null || !CNPJ_PATTERN.matcher(cnpj).matches()) {
            throw new ValidationException("CNPJ inválido! Informe somente números ou no formato 00.000.000/0000-00.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email inválido! Informe no padrão nome@dominio.com.br");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 6)
            throw new ValidationException("Senha deve ter ao mínimo 6 caracteres!");

        boolean upper = false, lower = false, digit = false;
        for(char c : password.toCharArray()) {
            if(Character.isUpperCase(c)) upper = true;
            if(Character.isLowerCase(c)) lower = true;
            if(Character.isDigit(c)) digit = true;
        }
        if(!upper || !lower || !digit) {
            throw new ValidationException("Senha deve possuir ao menos uma letra maiúscula, uma letra minúscula e um número!");
        }
    }
}
