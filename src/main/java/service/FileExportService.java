package service;

import model.Account;
import model.AccountPessoaFisica;
import model.Operation;
import model.Transfer;

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
                        op.getId(),
                        op.getAccountNumber(),
                        op.getSymbol(),
                        op.getTypeCode(),
                        op.getQuantity(),
                        op.getPrice(),
                        op.getDateTime().toString()
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
                        t.getId(),
                        t.getFromAccount() != null ? t.getFromAccount() : "",
                        t.getToAccount() != null ? t.getToAccount() : "",
                        t.getSymbol() != null ? t.getSymbol() : "",
                        t.getQuantity() != null ? String.format("%.4f", t.getQuantity()) : "",
                        t.getAmount() != null ? String.format("%.2f", t.getAmount()) : "",
                        t.getType(),
                        t.getDateTime().toString()
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
