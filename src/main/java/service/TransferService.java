package service;

import dao.TransferDAO;
import model.Account;
import model.AccountAsset;

import java.time.LocalDateTime;
import java.util.*;

public class TransferService {
    public void withdraw(Account contaOrigem, double valor) {
        if (!contaOrigem.withdraw(valor)) {
            System.out.println("Saldo insuficiente!");
            return;
        }

        adicionarTransferencia(
            contaOrigem.getAccountNumber(),
            null,
            null,
            0.00,
            valor,
            "SAQUE"
        );

        System.out.println("Saque efetuado!");
    }

    public void deposit(Account contaDestino, double valor) {
        if (valor <= 0) {
            System.out.println("Insira um valor válido!");
            return;
        }

        contaDestino.deposit(valor);

        adicionarTransferencia(
            null,
            contaDestino.getAccountNumber(),
            null,
            0.00,
            valor,
            "DEPOSITO"
        );
        System.out.println("Depósito realizado!");
    }

    public void transfer(Account contaOrigem, Account contaDestino, AccountAsset asset, double quantity) {
        String assetSymbol = asset.getAssetSymbol();
        double qtdOrigem = asset.getQuantity();

        if (qtdOrigem < quantity) {
            System.out.println("A conta não possui ativos suficientes para esta transferência!");
            return;
        }

        contaOrigem.removeAsset(asset, quantity);
        contaDestino.addAsset(asset, quantity);

        adicionarTransferencia(
            contaOrigem.getAccountNumber(),
            contaDestino.getAccountNumber(),
            assetSymbol,
            quantity,
            0.00,
            "TRANSFER_ATIVO");

        System.out.println("Transferência realizada!");
    }

    public List<record.Transfer> listByUser(String accountNumber) {
        TransferDAO dao = new TransferDAO();
        return dao.listByAccount(accountNumber);
    }

    private void adicionarTransferencia(String fromAccount, String toAccount, String symbol, Double quantity, Double amount, String type) {
        record.Transfer transfer = new record.Transfer(
            System.currentTimeMillis(),
            fromAccount,
            toAccount,
            symbol,
            quantity,
            amount,
            type,
            LocalDateTime.now()
        );

        TransferDAO dao = new TransferDAO();
        dao.insert(transfer);
    }
}
