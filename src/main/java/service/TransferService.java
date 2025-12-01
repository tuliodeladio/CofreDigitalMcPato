package service;

import model.Account;
import model.Transfer;

import java.time.LocalDateTime;
import java.util.*;

public class TransferService {

    private static Map<String, List<Transfer>> transferenciasPorConta = new HashMap<>();

    public Transfer withdraw(Account contaOrigem, double valor) {
        if (!contaOrigem.withdraw(valor)) {
            return null;
        }

        Transfer transferencia = new Transfer(
                System.currentTimeMillis(),
                contaOrigem.getAccountNumber(),
                null,
                null,
                null,
                valor,
                "SAQUE",
                LocalDateTime.now()
        );

        adicionarTransferencia(contaOrigem.getAccountNumber(), transferencia);
        return transferencia;
    }

    public Transfer deposit(Account contaDestino, double valor) {
        if (valor <= 0) {
            return null;
        }

        contaDestino.deposit(valor);

        Transfer transferencia = new Transfer(
                System.currentTimeMillis(),
                null,
                contaDestino.getAccountNumber(),
                null,
                null,
                valor,
                "DEPOSITO",
                LocalDateTime.now()
        );

        adicionarTransferencia(contaDestino.getAccountNumber(), transferencia);
        return transferencia;
    }

    public Transfer transfer(Account contaOrigem, Account contaDestino,
                             String symbol, double quantity) {

        double qtdOrigem = contaOrigem.getAsset(symbol);
        if (qtdOrigem < quantity) {
            return null;
        }

        contaOrigem.removeAsset(symbol, quantity);
        contaDestino.addAsset(symbol, quantity);

        Transfer transferencia = new Transfer(
                System.currentTimeMillis(),
                contaOrigem.getAccountNumber(),
                contaDestino.getAccountNumber(),
                symbol,
                quantity,
                null,
                "TRANSFER_ATIVO",
                LocalDateTime.now()
        );

        adicionarTransferencia(contaOrigem.getAccountNumber(), transferencia);
        adicionarTransferencia(contaDestino.getAccountNumber(), transferencia);
        return transferencia;
    }

    public List<Transfer> listByUser(String accountNumber) {
        return transferenciasPorConta.getOrDefault(accountNumber, new ArrayList<>());
    }

    private void adicionarTransferencia(String accountNumber, Transfer transferencia) {
        transferenciasPorConta
                .computeIfAbsent(accountNumber, k -> new ArrayList<>())
                .add(transferencia);
    }
}
