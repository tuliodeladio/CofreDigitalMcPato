package tests;

import dao.AccountDAO;
import model.Account;
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
                "12345678900",
                0.0
        );

        try {
            dao.insert(acc);
            System.out.println("Inserido!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // UPDATE
        acc.deposit(500);
        dao.update(acc);
        System.out.println("Atualizado!");

        // SELECT
        System.out.println("\nLista:");
        dao.listAll().forEach(a ->
                System.out.println(a.getAccountNumber() + " - " + a.getName())
        );

        // Find By Email
        System.out.println("\nBuscando conta por email teste@email.com");
        Account account = AccountDAO.findByEmail("teste@email.com");
        System.out.println(account.getAccountNumber() + " - " + account.getName() + " - " + account.getEmail());

        // DELETE
        dao.delete("9999999999");
        System.out.println("Deletado!");
    }
}