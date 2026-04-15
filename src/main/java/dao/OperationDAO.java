package dao;

import factory.ConnectionFactory;
import model.Operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {
    public void insert(Operation operation) {
        String sql = "INSERT INTO operation VALUES (?,?,?,?,?,?,?)";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, operation.getId());
            ps.setString(2, operation.getAccountNumber());
            ps.setString(3, operation.getSymbol());
            ps.setString(4, operation.getTypeCode());
            ps.setDouble(5, operation.getQuantity());
            ps.setDouble(6, operation.getQuantity());
            ps.setObject(7, operation.getDateTime());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Operation operation) {
        String sql = "UPDATE operation SET account_number = ?, asset_symbol = ?, operation_type = ?, quantity = ?, price = ? WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, operation.getAccountNumber());
            ps.setString(2, operation.getSymbol ());
            ps.setString(3, operation.getType().toString());
            ps.setDouble(4, operation.getQuantity());
            ps.setDouble(5, operation.getQuantity());
            ps.setLong(6, operation.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Operation operation) {
        String sql = "DELETE FROM operation WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, operation.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Operation> listByAccount(String accountNumber) {
        List<Operation> lista = new ArrayList<>();
        String sql = "SELECT * FROM operation WHERE account_number = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Operation.Type op_type = Operation.Type.fromCode(rs.getString("operation_type"));
                LocalDateTime created_at = rs.getTimestamp("created_at").toLocalDateTime();

                Operation op = new Operation(
                    rs.getLong("id"),
                    rs.getString("account_number"),
                    rs.getString("asset_symbol"),
                    op_type,
                    rs.getDouble("quantity"),
                    rs.getDouble("price"),
                    created_at
                );

                lista.add(op);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
