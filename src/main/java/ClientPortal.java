import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientPortal {

    // A method for account holder verification will be added in the future

    public static void authenticateAndShowMenu(Scanner scan,TransactionManager manager)
    {
        if (scan.hasNextLine()) // Consumes any leftover newline from the home page scanner.next()
        {
            scan.nextLine();
        }
        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
        System.out.println(" ");
        System.out.print("YOUR ACCOUNT NUMBER : ");
        String accNum= scan.next();
        // Like Employee portal a looped verification process (with id and password) will be added in the future
        // now I only checks for account existence
        char opt = 'y';
        while(!manager.doesAccountExist(accNum))
        {
            if(opt=='y')
            {
                System.out.println(" ");
                System.out.println("-----Unable to locate account or establish server connection. Process aborted.-----");
            }
            System.out.print("Do you want to continue and retry (y/n) : ");
            opt=Character.toLowerCase(scan.next().charAt(0));
            switch (opt)
            {
                case 'n':

                    if (scan.hasNextLine()) // Consumes any leftover newline from the accNum scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("----------------------------------------------------------------------");
                    return;

                case 'y':

                    if (scan.hasNextLine()) // Consumes any leftover newline from the accNum scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.print("YOUR ACCOUNT NUMBER : ");
                    accNum= scan.next();
                    break;

                default :
                    if (scan.hasNextLine()) // Consumes any leftover newline from the accNum scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please enter a valid option-----");

            }

        }

        if (scan.hasNextLine()) // Consumes any leftover newline from the accNum scan.next()
        {
            scan.nextLine();
        }
        boolean closeClientPortal = false;
        System.out.println(" ");
        System.out.println("----- Hi!, "+ accNum +" ----- "); // In the future account holder name will be fetched from database and displayed here instead of account number
        System.out.println(" ");
        System.out.println("1. SEND MONEY");
        System.out.println("2. MINI STATEMENT");
        System.out.println("3. Exit/Close ");
        System.out.println(" ");
        System.out.print("Enter your option number : ");
        while(!closeClientPortal)
        {
            char choice=scan.next().charAt(0);
            switch (choice)
            {
                case '1' :

                    if (scan.hasNextLine()) // Consumes any leftover newline from the choice scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.print("Please enter the receiver account number : ");
                    String receiverAccNum = scan.next();
                    System.out.print("Please enter the transaction amount : ");
                    try
                    {
                        double transferAmount = scan.nextDouble();
                        if (scan.hasNextLine()) // Consume the leftover newline after a successful double input
                        {
                            scan.nextLine();
                        }
                        manager.processTransfer(accNum,receiverAccNum,transferAmount);
                    }
                    catch (InputMismatchException e)
                    {
                        System.out.println(" ");
                        System.out.println("**Invalid input. Please enter a valid amount(in numbers) and try again.**");
                        scan.nextLine(); // Consume the invalid text token so it doesn't break the next menu
                    }

                    System.out.println(" ");
                    System.out.println("---------- MAIN MENU ----------");
                    System.out.println(" ");
                    System.out.println("1. SEND MONEY");
                    System.out.println("2. MINI STATEMENT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;

                case '2' :

                    if (scan.hasNextLine()) // Consumes any leftover newline from the choice scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    manager.printMiniStatement(accNum);
                    System.out.println(" ");
                    System.out.println("---------- MAIN MENU ----------");
                    System.out.println(" ");
                    System.out.println("1. SEND MONEY");
                    System.out.println("2. MINI STATEMENT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;

                case '3' :
                    if (scan.hasNextLine()) // Consumes any leftover newline from the choice scan.next()
                    {
                        scan.nextLine();
                    }
                    closeClientPortal=true;
                    break;

                default :
                    if (scan.hasNextLine()) // Consumes any leftover newline from the choice scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please select a valid option-----");
                    System.out.println(" ");
                    System.out.println("1. SEND MONEY");
                    System.out.println("2. MINI STATEMENT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
            }

        }
        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
    }
}
