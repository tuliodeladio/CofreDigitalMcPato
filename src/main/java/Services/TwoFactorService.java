package Services;

import java.util.Random;

public class TwoFactorService {
    private String code = null;

    public void generateAndSendCode(String email) {
        code = String.format("%06d", new Random().nextInt(1_000_000));
        System.out.println("Código de autenticação enviado para " + email + ": " + code);
    }

    public boolean validateCode(String inputCode) {
        return code != null && code.equals(inputCode);
    }
}
