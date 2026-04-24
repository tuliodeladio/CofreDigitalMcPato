package service;

import model.Account;
import model.AccountPessoaFisica;
import record.Operation;
import record.Transfer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class FileExportService {

    public void salvarOperacoesEmArquivo(List<Operation> operacoes, String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, false))) {
            for (Operation op : operacoes) {
                out.printf("%d;%s;%s;%s;%.4f;%.2f;%s%n",
                        op.id(),
                        op.accountNumber(),
                        op.symbol(),
                        op.getTypeCode(),
                        op.quantity(),
                        op.price(),
                        op.dateTime().toString()
                );
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar operacoes: " + e.getMessage());
        }
    }

    public void salvarTransferenciasEmArquivo(List<Transfer> transferencias, String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, false))) {
            for (Transfer t : transferencias) {
                out.printf("%d;%s;%s;%s;%s;%s;%s;%s%n",
                        t.id(),
                        t.fromAccount() != null ? t.fromAccount() : "",
                        t.toAccount() != null ? t.toAccount() : "",
                        t.symbol() != null ? t.symbol() : "",
                        t.quantity() != null ? String.format("%.4f", t.quantity()) : "",
                        t.amount() != null ? String.format("%.2f", t.amount()) : "",
                        t.getTypeCode(),
                        t.dateTime().toString()
                );
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar transferencias: " + e.getMessage());
        }
    }

    public void salvarContasEmArquivo(Map<String, Account> accountMap, String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, false))) {
            for (Account acc : accountMap.values()) {
                String tipo = (acc instanceof AccountPessoaFisica) ? "F" : "E";
                out.printf("%s;%s;%s;%.2f;%s%n",
                        acc.getAccountNumber(),
                        acc.getName(),
                        acc.getEmail(),
                        acc.getBalance(),
                        tipo
                );
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar contas: " + e.getMessage());
        }
    }
}
