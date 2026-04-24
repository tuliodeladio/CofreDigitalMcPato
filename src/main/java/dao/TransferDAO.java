package dao;

import factory.ConnectionFactory;
import record.Transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransferDAO {
    public void insert(Transfer transfer) {
        String sql = "INSERT INTO transfer VALUES (?,?,?,?,?,?,?,?)";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, transfer.id());
            ps.setString(2, transfer.fromAccount());
            ps.setString(3, transfer.toAccount());
            ps.setString(4, transfer.symbol());
            ps.setDouble(5, transfer.quantity());
            ps.setDouble(6, transfer.amount());
            ps.setObject(7, transfer.type());
            ps.setObject(8, transfer.dateTime());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retorna todas as transferências envolvendo a conta correspondente ao número fornecido.
    public List<Transfer> listByAccount(String accountNumber) {
        List<Transfer> lista = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE from_account = ? OR to_account = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ps.setString(2, accountNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transfer transfer = new Transfer(
                    rs.getLong("id"),
                    rs.getString("from_account"),
                    rs.getString("to_account"),
                    rs.getString("asset_symbol"),
                    rs.getDouble("quantity"),
                    rs.getDouble("transfer_value"),
                    rs.getString("transfer_type"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );

                lista.add(transfer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;

    }

    // Outros métodos
    // Apenas para exemplificar a aplicação de atualizações e deleções no banco de dados,
    // pois o suporte à essas funcionalidades não é interessante para as regras de negócio do sistema.

    public void update(Transfer transfer) {
        String sql =
            """
            UPDATE transfer SET from_account = ?, to_account = ?, asset_symbol = ?, quantity = ?, transfer_value = ?, transfer_type WHERE id = ?
            """;

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, transfer.fromAccount());
            ps.setString(2, transfer.toAccount());
            ps.setString(3, transfer.symbol());
            ps.setDouble(4, transfer.quantity());
            ps.setDouble(5, transfer.amount());
            ps.setLong(6, transfer.id());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Transfer transfer) {
        String sql = "DELETE FROM transfer WHERE id = ?";

        try(Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, transfer.id());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
