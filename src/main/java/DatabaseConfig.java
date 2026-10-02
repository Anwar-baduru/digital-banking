import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    public static Connection getConnection() throws SQLException
    {
        if(URL==null || USER==null || PASS==null)
        {
            throw new SQLException("CRITICAL ERROR: Local database environment credentials are not configured inside your IDE's Environment variables.");
        }
        return DriverManager.getConnection(URL,USER,PASS);
    }
    public static void initializeDatabase()
    {
        // To store unique branch codes for unique city, region pairs.
        // To avoid duplicate (city,region) pair -> PRIMARY KEY (city, region)
        // To avoid duplicate branch code -> CONSTRAINT unique_branch_code UNIQUE (branch_code)
        String createBranchTable= """
                CREATE TABLE IF NOT EXISTS branch_registry (
                city VARCHAR(50),
                region VARCHAR(50),
                branch_code VARCHAR(4),
                PRIMARY KEY (city, region),
                CONSTRAINT unique_branch_code UNIQUE (branch_code)
                );
                """;

        // To store account holder details but right now it only stores account number, account holder name and his/her balance.
        // In future we can add additional details like phone number for login, address, etc.
        String createAccountsTable= """
                CREATE TABLE IF NOT EXISTS bank_accounts (
                account_number VARCHAR(15) PRIMARY KEY,
                account_holder_name VARCHAR(100) NOT NULL,
                balance DECIMAL(15,2) NOT NULL
                );
                """;

        // To store transaction history details
        String createLedgerTable= """
                CREATE TABLE IF NOT EXISTS transaction_ledger (
                id INT AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(15),
                transaction_details VARCHAR(255) NOT NULL,
                send_to VARCHAR(30) DEFAULT NULL,
                receive_from VARCHAR(30) DEFAULT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_number) REFERENCES bank_accounts(account_number) ON DELETE CASCADE
                );
                """;

        // Connecting to Database and executing SQL queries
        try(Connection conn = getConnection(); Statement stmt = conn.createStatement())
        {
            stmt.execute(createBranchTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createLedgerTable);
            System.out.println("Database Bootstrapper : All tables and constraints are verified");

        } catch (SQLException e)
        {
            System.out.println("Bootstrapper Failure : "+e.getMessage());
        }
    }
}
