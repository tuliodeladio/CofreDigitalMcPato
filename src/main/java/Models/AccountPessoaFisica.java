package Models;

public class AccountPessoaFisica extends Account {

    public AccountPessoaFisica(String accountNumber, String name, String email, String passwordHash, String cpf) {
        super(accountNumber, name, email, passwordHash);
    }
}
