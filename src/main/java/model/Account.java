package model;

import dao.AccountAssetDAO;
import dao.AccountDAO;

import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected String documentNumber;
    protected String accountType;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected double balance;
    protected List<AccountAsset> wallet;

    public Account(String accountNumber, String name, String email, String passwordHash, String documentNumber, String accountType, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.balance = balance;
        this.documentNumber = documentNumber;
        this.accountType = accountType;

        AccountAssetDAO dao = new AccountAssetDAO();
        this.wallet = dao.listAllByAccount(accountNumber);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public double getBalance() {
        AccountDAO dao = new AccountDAO();
        return dao.getBalance(accountNumber);
    }

    public void deposit(double amount) {
        balance += amount;

        AccountDAO dao = new AccountDAO();
        dao.updateBalance(accountNumber, balance);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;

        AccountDAO dao = new AccountDAO();
        dao.updateBalance(accountNumber, balance);

        return true;
    }

    public List<AccountAsset> getWallet() {
        return wallet;
    }

    public double getAssetQuantity(String symbol) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset asset = dao.findAccountAsset(this.accountNumber, symbol);

        return asset != null ? asset.getQuantity() : 0.0;
    }

    public void addAsset(String assetSymbol, double quantity) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(this.accountNumber, assetSymbol);

        // Se o usuário já possui o asset, atualiza a quantidade, caso contrário, insere um novo registro.
        if (accountAsset == null) {
            accountAsset = new AccountAsset(this.accountNumber, assetSymbol, quantity);
            dao.insert(accountAsset);
        } else {
            accountAsset.setQuantity(accountAsset.getQuantity() + quantity);
            dao.update(accountAsset);
        }
    }

    public void removeAsset(String assetSymbol, double quantity) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(this.accountNumber, assetSymbol);

        // Se o asset não existe no banco, a quantidade atual é definida como 0.
        double qtdAtual = accountAsset == null ? 0.0 : accountAsset.getQuantity();

        // Para remover o asset, ele precisa existir no banco, e a quantidade a ser removida precisa ser:
        //   - maior que 0
        //   - menor ou igual à quantidade atual
        if ((accountAsset != null) && (qtdAtual >= quantity) && (quantity > 0)) {
            accountAsset.setQuantity(qtdAtual - quantity);

            if (accountAsset.getQuantity() == 0) {
                dao.delete(accountAsset);
            } else {
                dao.update(accountAsset);
            }
        } else {
            System.out.println("A conta não possui ativos suficientes para esta operação!");
        }
    }
}