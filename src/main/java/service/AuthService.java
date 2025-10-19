package service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import model.Account;

public class AuthService {
    private final Map<String, Account> accounts = new HashMap<>();

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void register(Account account) {
        accounts.put(account.getEmail(), account);
    }

    public Account login(String email, String password) {
        Account account = accounts.get(email);
        if (account == null) return null;
        String hash = hashPassword(password);
        if (account.getPasswordHash().equals(hash)) {
            return account;
        }
        return null;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
