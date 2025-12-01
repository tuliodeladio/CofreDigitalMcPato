package model;

public class AccountEmpresa extends Account {
    private String cnpj;

    public AccountEmpresa(String accountNumber, String name, String email,
                          String passwordHash, String cnpj) {
        super(accountNumber, name, email, passwordHash);
        this.cnpj = cnpj;
    }

    public String getCnpj() { return cnpj; }
}
