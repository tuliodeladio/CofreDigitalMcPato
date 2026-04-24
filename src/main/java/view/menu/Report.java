package view.menu;

import model.Account;
import record.Operation;
import service.OperationService;
import service.ReportService;
import service.TransferService;

import java.util.List;

public class Report {
    public static void menuHandler(Account currentUser) {
        OperationService operationService = new OperationService();
        TransferService transferService = new TransferService();
        ReportService reportService = new ReportService();

        List<Operation> minhasOps = operationService.listByAccount(currentUser.getAccountNumber());
        List<record.Transfer> minhasTransfs = transferService.listByUser(currentUser.getAccountNumber());

        reportService.printReport(
            currentUser.getAccountNumber(),
            minhasOps,
            currentUser.getBalance(),
            minhasTransfs
        );
    }
}
