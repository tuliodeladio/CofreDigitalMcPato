package dao;

import factory.ConnectionFactory;
import record.Asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssetDAO {

    public void insert(Asset asset) {
        String sql = "INSERT INTO asset (ASSET_SYMBOL, ASSET_NAME, ASSET_CURRENT_VALUE) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, asset.symbol());
            ps.setString(2, asset.name());
            ps.setDouble(3, asset.currentValue());

            int rows = ps.executeUpdate();
            System.out.println("[AssetDAO] Ativo inserido: " + asset.symbol() + " (linhas: " + rows + ")");

        } catch (SQLException e) {
            System.err.println("[AssetDAO] ERRO SQL: " + e.getMessage());
            System.err.println("[AssetDAO] Código: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[AssetDAO] ERRO Geral: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Asset> listAll() {
        List<Asset> lista = new ArrayList<>();

        String sql = "SELECT ASSET_SYMBOL AS symbol, ASSET_NAME AS name, ASSET_CURRENT_VALUE AS currentValue FROM asset";

        System.out.println("[AssetDAO] CONEXÃO: tentando abrir...");

        try (Connection con = ConnectionFactory.getConnection()) {
            System.out.println("[AssetDAO] CONEXÃO: aberta = " + (con != null && !con.isClosed()));

            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("[AssetDAO] PreparedStatement criado");

            ResultSet rs = ps.executeQuery();
            System.out.println("[AssetDAO] executeQuery() executado");

            // ← VERIFICAR SE TEM LINHAS
            System.out.println("[AssetDAO] Verificando se ResultSet tem linhas...");
            int count = 0;

            while (rs.next()) {
                count++;
                System.out.println("[AssetDAO] LINHA " + count + " encontrada!");

                String symbol = rs.getString("symbol");
                String name = rs.getString("name");
                double value = rs.getDouble("currentValue");

                System.out.println("[AssetDAO] Dados: " + symbol + " | " + name + " | " + value);

                Asset asset = new Asset(symbol, name, value);
                lista.add(asset);
            }

            System.out.println("[AssetDAO] Total de linhas lidas: " + count);
            System.out.println("[AssetDAO] Total de ativos na lista: " + lista.size());

        } catch (SQLException e) {
            System.err.println("[AssetDAO] ERRO SQL: " + e.getMessage());
            System.err.println("[AssetDAO] Código: " + e.getErrorCode());
            System.err.println("[AssetDAO] Estado: " + e.getSQLState());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[AssetDAO] ERRO Geral: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[AssetDAO] Returning lista com " + lista.size() + " elementos");
        return lista;
    }

    public Asset findBySymbol(String assetSymbol) {
        String sql = "SELECT ASSET_SYMBOL AS symbol, ASSET_NAME AS name, ASSET_CURRENT_VALUE AS currentValue FROM asset WHERE ASSET_SYMBOL = ?";
        Asset asset = null;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, assetSymbol);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                asset = new Asset(
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("currentValue")
                );
                System.out.println("[AssetDAO] Ativo encontrado: " + asset.symbol());
            } else {
                System.out.println("[AssetDAO] Ativo NÃO encontrado: " + assetSymbol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return asset;
    }

    public void update(Asset asset) {
        String sql = "UPDATE asset SET ASSET_CURRENT_VALUE = ? WHERE ASSET_SYMBOL = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, asset.currentValue());
            ps.setString(2, asset.symbol());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String assetSymbol) {
        String sql = "DELETE FROM asset WHERE ASSET_SYMBOL = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, assetSymbol);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAllPrices() {
        String sql = "UPDATE asset SET ASSET_CURRENT_VALUE = ASSET_CURRENT_VALUE * ?";
        double factor = 1 + ((Math.random() - 0.5) * 0.05);

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, factor);

            ps.executeUpdate();
            System.out.println("[AssetDAO] Preços atualizados (fator: " + factor + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}