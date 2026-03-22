package app;

import dao.AccountDAO;
import model.AccountPessoaFisica;

public class TesteDB {

    public static void main(String[] args) {

        AccountDAO dao = new AccountDAO();

        // INSERT
        AccountPessoaFisica acc = new AccountPessoaFisica(
                "9999999999",
                "Teste",
                "teste@email.com",
                "hash123",
                "12345678900"
        );

        dao.insert(acc);
        System.out.println("Inserido!");

        // UPDATE
        acc.deposit(500);
        dao.update(acc);
        System.out.println("Atualizado!");

        // SELECT
        System.out.println("\nLista:");
        dao.listAll().forEach(a ->
                System.out.println(a.getAccountNumber() + " - " + a.getName())
        );

        // DELETE
        dao.delete("9999999999");
        System.out.println("Deletado!");
    }
}