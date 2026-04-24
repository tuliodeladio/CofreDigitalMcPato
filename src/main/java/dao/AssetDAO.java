package dao;

import factory.ConnectionFactory;
import model.Asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AssetDAO {

    public void insert(Asset asset) {
        String sql = "INSERT INTO asset VALUES (?,?,?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, asset.getSymbol());
            ps.setString(2, asset.getName());
            ps.setDouble(3, asset.getCurrentValue());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Só permite atualizar o valor do ativo
    public void update(Asset asset) {
        String sql = "UPDATE asset SET asset_current_value = ? WHERE asset_symbol = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, asset.getCurrentValue());
            ps.setString(2, asset.getSymbol());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String assetSymbol) {
        String sql = "DELETE FROM asset WHERE asset_symbol = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, assetSymbol);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Asset> listAll() {
        List<Asset> lista = new ArrayList<>();

        String sql = "SELECT * FROM asset";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Asset asset = new Asset(
                        rs.getString("asset_symbol"),
                        rs.getString("asset_name"),
                        rs.getDouble("asset_current_value")
                );

                lista.add(asset);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Asset findBySymbol(String assetSymbol) {
        String sql = "SELECT * FROM asset WHERE asset_symbol = ?";
        Asset asset = null;

        try(
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, assetSymbol);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                asset = new Asset(assetSymbol, rs.getString("asset_name"), rs.getDouble("asset_current_value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return asset;
    }

    public void updateAllPrices() {
        String sql = "UPDATE asset SET asset_current_value = asset_current_value * ?";
        double factor = 1 + ((Math.random() - 0.5) * 0.05); // ±5%

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, factor);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}