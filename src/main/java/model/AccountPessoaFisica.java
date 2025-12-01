package model;

public class AccountPessoaFisica extends Account {
    private String cpf;

    public AccountPessoaFisica(String accountNumber, String name, String email,
                               String passwordHash, String cpf) {
        super(accountNumber, name, email, passwordHash);
        this.cpf = cpf;
    }

    public String getCpf() { return cpf; }
}
