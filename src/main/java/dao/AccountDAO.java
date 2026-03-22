package dao;

import model.AccountPessoaFisica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void insert(AccountPessoaFisica acc) {
        String sql = "INSERT INTO account VALUES (?,?,?,?,?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getName());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPasswordHash());
            ps.setDouble(5, acc.getBalance());
            ps.setString(6, "f");
            ps.setString(7, acc.getCpf());

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

    public List<AccountPessoaFisica> listAll() {
        List<AccountPessoaFisica> lista = new ArrayList<>();

        String sql = "SELECT * FROM account WHERE acc_type = 'f'";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AccountPessoaFisica acc = new AccountPessoaFisica(
                        rs.getString("acc_number"),
                        rs.getString("acc_name"),
                        rs.getString("acc_email"),
                        rs.getString("acc_password_hash"),
                        rs.getString("acc_document_number")
                );

                lista.add(acc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}