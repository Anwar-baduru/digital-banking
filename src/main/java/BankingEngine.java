
import java.util.Scanner;

public class BankingEngine {

    public static void main(String[] args) {

        System.out.println("=== STARTING BANKING ENGINE PLATFORM ===");

        // 1. Initialize and verify database tables on local hard drive
        DatabaseConfig.initializeDatabase();

        BranchRegistry branchRegistry = new BranchRegistry();
        TransactionManager transactionManager = new TransactionManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println(" ");
        System.out.println("********** WELCOME **********");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("1. EMPLOYEE");
        System.out.println("2. ACCOUNT HOLDER");
        System.out.println("3. Exit/Close ");
        System.out.println(" ");
        System.out.print("Enter your option number : ");
        boolean closeEngine = false;
        while(!closeEngine)
        {
            char option=scanner.next().charAt(0);
            switch (option)
            {
                case '1' :
                    EmployeePortal.authenticateAndShowMenu(scanner,branchRegistry,transactionManager);
                    System.out.println(" ");
                    System.out.println("---------- HOME PAGE ----------");
                    System.out.println(" ");
                    System.out.println("1. EMPLOYEE");
                    System.out.println("2. ACCOUNT HOLDER");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;
                case '2' :
                    ClientPortal.authenticateAndShowMenu(scanner,transactionManager);
                    System.out.println(" ");
                    System.out.println("---------- HOME PAGE ----------");
                    System.out.println(" ");
                    System.out.println("1. EMPLOYEE");
                    System.out.println("2. ACCOUNT HOLDER");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;
                case '3' :
                    closeEngine=true;
                    break;

                default :
                    if (scanner.hasNextLine()) // Consumes any leftover newline from the scanner.next()
                    {
                        scanner.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please select a valid option-----");
                    System.out.println(" ");
                    System.out.println("1. EMPLOYEE");
                    System.out.println("2. ACCOUNT HOLDER");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");

            }
        }
        System.out.println(" ");
        System.out.println("-----Thank you for using our bank service-----");

    }
}
