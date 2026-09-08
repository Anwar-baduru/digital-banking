import java.util.Stack;

public class BankAccount {
    String accountNumber;
    String accountHolderName;
    double balance;
    // we can add contact,address and other additional details in future
    Stack<String> transactionHistory = new Stack<>();
    public BankAccount(String accountNumber,String name,double initialBalance)
    {
        this.accountNumber=accountNumber;
        this.accountHolderName=name;
        this.balance=initialBalance;
        transactionHistory.push("Account opened with balance "+initialBalance);
    }

}
