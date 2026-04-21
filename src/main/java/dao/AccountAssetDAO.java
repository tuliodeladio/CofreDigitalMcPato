package dao;

import factory.ConnectionFactory;
import model.AccountAsset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountAssetDAO {

    public void insert(AccountAsset accountAsset) {
        String sql = "INSERT INTO account_asset VALUES (?,?,?)";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountAsset.getAccountNumber());
            ps.setString(2, accountAsset.getAssetSymbol());
            ps.setDouble(3, accountAsset.getQuantity());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(AccountAsset accountAsset) {
        String sql = "UPDATE account_asset SET quantity = ? WHERE account_number = ? AND asset_symbol = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, accountAsset.getQuantity());
            ps.setString(2, accountAsset.getAccountNumber());
            ps.setString(3, accountAsset.getAssetSymbol());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(AccountAsset accountAsset) {
        String sql = "DELETE FROM account_asset WHERE account_number = ? AND asset_symbol = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountAsset.getAccountNumber());
            ps.setString(2, accountAsset.getAssetSymbol());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AccountAsset findAccountAsset(String accountNumber, String assetSymbol) {
        String sql = "SELECT * FROM account_asset WHERE account_number = ? AND asset_symbol = ?";
        AccountAsset accountAsset = null;

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ps.setString(2, assetSymbol);

            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                accountAsset = new AccountAsset(
                    rs.getString("account_number"),
                    rs.getString("asset_symbol"),
                    rs.getDouble("quantity")
                );
            }

            return accountAsset;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountAsset;
    }

    public List<AccountAsset> listAllByAccount(String accountNumber) {
        List<AccountAsset> lista = new ArrayList<>();

        String sql = "SELECT * FROM account_asset WHERE account_number = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AccountAsset accountAsset = new AccountAsset(
                    rs.getString("account_number"),
                    rs.getString("asset_symbol"),
                    rs.getDouble("quantity")
                );

                lista.add(accountAsset);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}