package command;

import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionCommand implements Command {
    private List<Transaction> transactionsToRemove;
    private Account account;

    public RemoveTransactionCommand(List<Transaction> transactionsToRemove, Account account) {
        this.transactionsToRemove = transactionsToRemove;
        this.account = account;
    }
    @Override
    public void execute() {
        account.getTransactions().removeAll(transactionsToRemove);
    }

    @Override
    public String getName() {
        return "Removed " + transactionsToRemove.size() + " transactions";
    }

    @Override
    public void undo() {
        account.getTransactions().addAll(transactionsToRemove);
    }

    @Override
    public void redo() {
        account.getTransactions().removeAll(transactionsToRemove);
    }
}

