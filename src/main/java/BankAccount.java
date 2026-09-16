import java.util.ArrayDeque;
import java.util.Deque;

public class BankAccount {
    String accountNumber;
    String accountHolderName;
    double balance;
    // we can add contact,address and other additional details in future

    /* * Previously I used a stack to prevent financial fraud and the alteration of financial history.
       * But it gives a difficulty when there is a need to print only certain number of transaction histories
       * So in order to overcome this problem now I am using Deque which also provides same level of
         security and immutability
    * */
    Deque<String> transactionHistory = new ArrayDeque<>();
    public BankAccount(String accountNumber,String name,double initialBalance)
    {
        this.accountNumber=accountNumber;
        this.accountHolderName=name;
        this.balance=initialBalance;
        // Like stack adding elements directly to the head/top of the Deque
        transactionHistory.push("Account opened with balance "+initialBalance);
    }

}
