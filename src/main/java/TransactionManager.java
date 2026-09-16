
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TransactionManager {
    private Map<String,BankAccount> accounts = new HashMap<>();

    // Helper method to let the generator check if an account number already exists
    public boolean doesAccountExist(String accountNumber)
    {
        return accounts.containsKey(accountNumber);
    }

    // String's null value checking or empty string checking is not performed
    // Because I planned to get the values for city, bankRegion, accountType using list of options or selecting from a list
    // And pass it to this method
    public void createAccount(String name,String city,String bankRegion,String accountType,double initialDeposit,BranchRegistry registry)
    {

        AccountNumberGenerator generator = new AccountNumberGenerator();
        String accountNum = generator.generateAccountNumber(city,bankRegion,accountType,registry,this);
        if(accountNum==null||accountNum.isEmpty())
        {
            System.out.println("Failed to create an Account.");
            return;
        }
        accounts.put(accountNum,new BankAccount(accountNum,name,initialDeposit));
        System.out.println("Account "+accountNum+" created Successfully.");
    }

    public void processTransfer(String senderAcc, String receiverAcc, double amount)
    {
        BankAccount sender=accounts.get(senderAcc);
        BankAccount receiver=accounts.get(receiverAcc);
        if(sender==null)
        {
            System.out.println("Transaction Failed: Invalid account "+ senderAcc);
            return;
        }
        if(receiver==null)
        {
            System.out.println("Transaction Failed: Invalid account "+ receiverAcc);
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
        if(account==null)
        {
            System.out.println("Transaction Failed: Invalid account "+ accNumber);
            return;
        }
        Iterator<String> itr = account.transactionHistory.iterator();
        int count=0;
        int printingLimit=Math.min(10,account.transactionHistory.size());
        System.out.println("Statement for "+accNumber);
        System.out.println("Current Balance : "+ account.balance);
        while (itr.hasNext() && count < printingLimit)
        {
            System.out.println(itr.next());
            count++;
        }
    }
}
