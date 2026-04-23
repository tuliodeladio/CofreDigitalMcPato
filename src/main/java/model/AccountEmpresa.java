package model;

public class AccountEmpresa extends Account {
    private String cnpj;

    public AccountEmpresa(String accountNumber, String name, String email,
                          String passwordHash, String cnpj, double balance) {
        super(accountNumber, name, email, passwordHash, cnpj, "e", balance);
        this.cnpj = cnpj;
        this.accountType = "e";
    }

    public String getCnpj() { return cnpj; }
}
