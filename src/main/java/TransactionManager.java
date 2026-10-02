
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionManager {

    // Helper method to let the generator check if an account number already exists
    public boolean doesAccountExist(String accountNumber)
    {
        String sql = "SELECT 1 FROM bank_accounts WHERE account_number = ?";
        try(Connection conn=DatabaseConfig.getConnection();PreparedStatement pstmt= conn.prepareStatement(sql))
        {
            pstmt.setString(1,accountNumber);
            try(ResultSet rs=pstmt.executeQuery())
            {
                return rs.next();
            }
        } catch (SQLException e)
        {
            System.out.println("Database Access Error during account check: " + e.getMessage());
            return false;
        }
    }

    // Helper method to extract the name associated with an account.
    private String getAccountHolderName(Connection conn, String accountNumber) throws SQLException
    {
        String sql="SELECT account_holder_name FROM bank_accounts WHERE account_number=?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,accountNumber);
            try(ResultSet rs=pstmt.executeQuery())
            {
                if(rs.next())
                {
                    return rs.getString("account_holder_name");
                }
                else
                {
                    throw new SQLException("INVALID ACCOUNT : Account number " + accountNumber + " not found in the system records.");
                }
            }
        }
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

        String sql="INSERT INTO bank_accounts (account_number,account_holder_name,balance) VALUES (?,?,?)";
        String ledgerSql = "INSERT INTO transaction_ledger (account_number, transaction_details, send_to, receive_from) VALUES (?,?,?,?)";
        try(Connection conn=DatabaseConfig.getConnection(); PreparedStatement pstmt=conn.prepareStatement(sql);
            PreparedStatement ledgerPstmt = conn.prepareStatement(ledgerSql))
        {
            pstmt.setString(1,accountNum);
            pstmt.setString(2,name);
            pstmt.setDouble(3,initialDeposit);
            pstmt.executeUpdate();

            ledgerPstmt.setString(1,accountNum);
            ledgerPstmt.setString(2,"Account opened with balance " + initialDeposit);
            ledgerPstmt.setNull(3,java.sql.Types.VARCHAR);
            ledgerPstmt.setNull(4,java.sql.Types.VARCHAR);
            ledgerPstmt.executeUpdate();

            System.out.println("Account "+accountNum+" created Successfully.");
        } catch (SQLException e)
        {
            System.out.println("Database Access Error during Account Creation : "+e.getMessage());
        }
    }

    /**
     * Processes a money transfer securely.
     * If the system crashes right after debiting the sender but before crediting the receiver, the money would disappear completely.
     * So by turning off AutoCommit, we force MySQL to wait.
     * Only when the debit, the credit, and both ledger logs execute perfectly, we invoke conn.commit().
     * If any single step fails, conn.rollback() restores the money instantly.
     */

    public void processTransfer(String senderAcc, String receiverAcc, double amount)
    {
        String debitSql = "UPDATE bank_accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        String creditSql = "UPDATE bank_accounts SET balance = balance + ? WHERE account_number = ?";
        String ledgerSql = "INSERT INTO transaction_ledger (account_number, transaction_details, send_to, receive_from) VALUES (?,?,?,?)";

        try(Connection conn=DatabaseConfig.getConnection())
        {
            // Turn off auto-commit to group and handle multiple SQL statements into a single transaction
            conn.setAutoCommit(false);

            try(PreparedStatement debitPstmt = conn.prepareStatement(debitSql);
                PreparedStatement creditPstmt = conn.prepareStatement(creditSql);
                PreparedStatement ledgerPstmt = conn.prepareStatement(ledgerSql))
            {
                // If either of these lines fail or throw an exception, execution jumps instantly to the catch block below
                String senderName = getAccountHolderName(conn,senderAcc);
                String receiverName = getAccountHolderName(conn,receiverAcc);

                // Process Debit
                debitPstmt.setDouble(1,amount);
                debitPstmt.setString(2,senderAcc);
                debitPstmt.setDouble(3,amount);
                int rowsDebited = debitPstmt.executeUpdate();
                if (rowsDebited == 0)
                {
                    System.out.println("Transaction Failed : Invalid sender account or Insufficient Balance.");
                    conn.rollback();
                    return;
                }

                // Process Credit
                creditPstmt.setDouble(1,amount);
                creditPstmt.setString(2,receiverAcc);
                int rowsCredited = creditPstmt.executeUpdate();
                if (rowsCredited == 0)
                {
                    System.out.println("Transaction Failed : Invalid receiver account "+receiverAcc+".");
                    conn.rollback();
                    return;
                }

                // Sender History Log Entry
                ledgerPstmt.setString(1,senderAcc);
                ledgerPstmt.setString(2,"Debited: " + amount + " to " + receiverName);
                ledgerPstmt.setString(3,receiverAcc);
                ledgerPstmt.setNull(4,java.sql.Types.VARCHAR);
                ledgerPstmt.executeUpdate();

                // Receiver History Log Entry
                ledgerPstmt.setString(1,receiverAcc);
                ledgerPstmt.setString(2,"Credited: " + amount + " from " + senderName);
                ledgerPstmt.setNull(3,java.sql.Types.VARCHAR);
                ledgerPstmt.setString(4,senderAcc);
                ledgerPstmt.executeUpdate();

                conn.commit(); // Securely save changes to disk
                System.out.println("Successfully transferred " + amount + " from " + senderName + " to " + receiverName);

            } catch (SQLException innerEx) // Catches any database connectivity drops inside inner try or helper lookups exceptions
            {
                conn.rollback(); // Restores money states instantly
                System.out.println("Transaction Terminated : " + innerEx.getMessage());
            }
        } catch (SQLException e)
        {
            System.out.println("Database Connection Failure : " + e.getMessage());
        }
    }

    public void printMiniStatement(String accNumber)
    {
        String accountDetailsSql="SELECT balance FROM bank_accounts WHERE account_number = ?";
        String ledgerHistorySql="SELECT transaction_details FROM transaction_ledger WHERE account_number = ? ORDER BY timestamp DESC LIMIT 10";
        try(Connection conn=DatabaseConfig.getConnection();PreparedStatement accountPstmt=conn.prepareStatement(accountDetailsSql);
            PreparedStatement ledgerPstmt=conn.prepareStatement(ledgerHistorySql))
        {
            accountPstmt.setString(1,accNumber);
            try(ResultSet accountRs=accountPstmt.executeQuery())
            {
                if(!accountRs.next())
                {
                    System.out.println("Transaction Failed: Invalid account " + accNumber);
                    return;
                }
                System.out.println("----------------------------------------------------------------------");
                System.out.println("Statement for " + accNumber);
                System.out.println("Current Balance  : " + accountRs.getDouble("balance"));
            }

            ledgerPstmt.setString(1, accNumber);
            try(ResultSet ledgerRs=ledgerPstmt.executeQuery())
            {
                System.out.println(" ");
                while(ledgerRs.next())
                {
                    System.out.println(" "+ledgerRs.getString("transaction_details"));
                }
                System.out.println(" ");
                System.out.println("----------------------------------------------------------------------");
            }
        } catch (SQLException e)
        {
            System.out.println("Database Access Error during mini-statement output: " + e.getMessage());
        }
    }
}
