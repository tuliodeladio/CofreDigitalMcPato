package model;

public class AccountPessoaFisica extends Account {

    public AccountPessoaFisica(String accountNumber, String name, String email, String passwordHash, String cpf) {
        super(accountNumber, name, email, passwordHash);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Atenção: valores de saque podem ser reportados e deverão ser verificados quanto à declaração de impostos pessoais.");
        return super.withdraw(amount);
    }
}
