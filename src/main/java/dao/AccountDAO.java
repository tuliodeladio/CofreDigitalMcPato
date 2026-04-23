package dao;

import factory.ConnectionFactory;
import model.Account;
import model.AccountPessoaFisica;
import model.AccountEmpresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void insert(Account acc) throws SQLException {
        String sql = "INSERT INTO account VALUES (?,?,?,?,?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getName());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPasswordHash());
            ps.setDouble(5, acc.getBalance());
            ps.setString(6, acc.getAccountType());
            ps.setString(7, acc.getDocumentNumber());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(AccountPessoaFisica acc) {
        String sql = "UPDATE account SET acc_name=?, acc_email=?, acc_balance=? WHERE acc_number=?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, acc.getName());
            ps.setString(2, acc.getEmail());
            ps.setDouble(3, acc.getBalance());
            ps.setString(4, acc.getAccountNumber());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String accountNumber) {
        String sql = "DELETE FROM account WHERE acc_number=?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Account> listAll() {
        List<Account> lista = new ArrayList<>();
        String sql = "SELECT * FROM account";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapAccount(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Account findByEmail(String email) {
        String sql = "SELECT * FROM account WHERE acc_email = ?";
        Account acc = null;

        try(
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                acc = mapAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return acc;
    }

    public void updateBalance(String accountNumber, double balance) {
        String sql = "UPDATE account SET acc_balance = ? WHERE acc_number = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, balance);
            ps.setString(2, accountNumber);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Account mapAccount(ResultSet rs) throws SQLException {
        String account_type = rs.getString("acc_type");
        String account_number = rs.getString("acc_number");
        String name = rs.getString("acc_name");
        String pwd_hash = rs.getString("acc_password_hash");
        String doc_number = rs.getString("acc_document_number");
        String email = rs.getString("acc_email");
        double balance = rs.getDouble("acc_balance");

        return account_type.equalsIgnoreCase("f")
            ? new AccountPessoaFisica(account_number, name, email, pwd_hash, doc_number, balance)
            : new AccountEmpresa(account_number, name, email, pwd_hash, doc_number, balance);
    }
}