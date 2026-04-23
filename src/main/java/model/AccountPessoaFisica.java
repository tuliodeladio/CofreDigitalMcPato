package model;

public class AccountPessoaFisica extends Account {
    private String cpf;

    public AccountPessoaFisica(String accountNumber, String name, String email,
                               String passwordHash, String cpf, double balance) {
        super(accountNumber, name, email, passwordHash, cpf, "f", balance);
        this.cpf = cpf;
        this.accountType = "f";
    }

    public String getCpf() { return cpf; }
}
