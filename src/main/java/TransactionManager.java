
import java.util.HashMap;
import java.util.Map;

public class TransactionManager {
    private Map<String,BankAccount> accounts=new HashMap<>();
    public void createAccount(String accountNum,String name,double initialDeposit)
    {
        accounts.put(accountNum,new BankAccount(accountNum,name,initialDeposit));
        System.out.println("Account "+accountNum+" created Successfully.");
    }
    public void processTransfer(String senderAcc, String receiverAcc, double amount)
    {
        BankAccount sender=accounts.get(senderAcc);
        BankAccount receiver=accounts.get(receiverAcc);
        if(sender==null)
        {
            System.out.println("Transaction Failed: Invalid account "+senderAcc);
            return;
        }
        if(receiver==null)
        {
            System.out.println("Transaction Failed: Invalid accounts "+receiverAcc);
            return;
        }
        if(sender.balance<amount)
        {
            System.out.println("Transaction Failed: Insufficient Balance.");
            return;
        }
        sender.balance-=amount;
        receiver.balance+=amount;
        sender.transactionHistory.push("Debited: " + amount + " to " + receiverAcc);
        receiver.transactionHistory.push("Credited: " + amount + " from " + senderAcc);
        System.out.println("Successfully transferred " + amount + " from " + sender.accountHolderName + " to " + receiver.accountHolderName);
    }
    public void printMiniStatement(String accNumber)
    {
        BankAccount account=accounts.get(accNumber);
        if(account!=null)
        {
            System.out.println("Statement for "+accNumber);
            System.out.println("Current Balance : "+account.balance);
            if(account.transactionHistory.size()<10)
            {
                for(String log:account.transactionHistory)
                {
                    System.out.println(log);
                }
            }
        }
    }
}
