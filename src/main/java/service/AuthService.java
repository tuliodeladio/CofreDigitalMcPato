package service;

import java.security.MessageDigest;
import java.sql.SQLException;
import dao.AccountDAO;
import model.Account;

public class AuthService {
    public void register(Account account) {
        AccountDAO dao = new AccountDAO();

        try {
            dao.insert(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account login(String email, String password) {
        Account account = AccountDAO.findByEmail(email);

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
