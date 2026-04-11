package model;

public class AccountPessoaFisica extends Account {
    private String cpf;

    public AccountPessoaFisica(String accountNumber, String name, String email,
                               String passwordHash, String cpf) {
        super(accountNumber, name, email, passwordHash, cpf, "f");
        this.cpf = cpf;
        this.accountType = "f";
    }

    public String getCpf() { return cpf; }
}
