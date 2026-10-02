import java.util.InputMismatchException;
import java.util.Scanner;

public class EmployeePortal {

    private static boolean verifyEmployee(String empId,String password)
    {
        /* Currently it only checks for 1 default employee id.
         * In the future we can update this method to verify employee ids from employee database*/
        if(empId==null||empId.isEmpty()||password==null||password.isEmpty())return false;
        return empId.equals("Admin")&&password.equals("samplepass123");
    }

    private static String normalizeString(String userInput,String fieldName,Scanner scan)
    {
        String normalizedOutput = userInput;
        if(fieldName.equalsIgnoreCase("clientName"))
        {
            normalizedOutput = normalizedOutput.trim().replaceAll("\\s+", " ").toUpperCase();
            while(normalizedOutput.isBlank())
            {
                System.out.println("** Field should not be Empty/Invalid **");
                System.out.println(" ");
                System.out.print("Please enter a valid FULL NAME : ");
                normalizedOutput = scan.nextLine().trim().replaceAll("\\s+", " ").toUpperCase();
            }
            return normalizedOutput;
        }
        if(fieldName.equalsIgnoreCase("branch code"))
        {
            while(normalizedOutput.length() != 4)
            {
                System.out.println("** Branch Code should not be Empty/Greater than 4 digits/Less than 4 digits **");
                System.out.println(" ");
                System.out.print("Please enter a valid BRANCH CODE : ");
                normalizedOutput = scan.next();
            }
            return normalizedOutput;
        }
        normalizedOutput = normalizedOutput.trim().replaceAll("\\s+", " ");
        normalizedOutput=normalizedOutput.replaceAll("[^a-zA-Z ]", "");
        while(normalizedOutput.isBlank())
        {
            System.out.println("** Field should not be Empty/Invalid **");
            System.out.println(" ");
            System.out.print("Please enter a valid "+fieldName+" : ");
            normalizedOutput = scan.nextLine().trim().replaceAll("\\s+", " ");
            normalizedOutput = normalizedOutput.replaceAll("[^a-zA-Z ]", "");

        }
        return normalizedOutput;
    }

    private static void processAccountOpening(Scanner scan,BranchRegistry registry,TransactionManager manager)
    {

        if (scan.hasNextLine()) // Consumes any leftover newline from the main menu's scan.next()
        {
            scan.nextLine();
        }

        System.out.println(" ");
        System.out.println("----- Please enter the following details -----");
        System.out.println(" ");
        System.out.print("FULL NAME : ");
        String clientName = scan.nextLine();
        clientName = normalizeString(clientName,"clientName",scan);

        System.out.print("CITY : ");
        String city = scan.nextLine();      // future it will be replaced by drop down list options
        city = normalizeString(city,"CITY",scan);

        System.out.print("REGION : ");
        String region = scan.nextLine();   // future it will be replaced by drop down list options
        region = normalizeString(region,"REGION",scan);

        System.out.print("BANKING TYPE (savings/business/current) : ");
        String bankAccType = scan.next(); // future it will be replaced by drop down list options
        bankAccType = normalizeString(bankAccType,"BANKING TYPE",scan);

        System.out.print("INITIAL DEPOSIT AMOUNT : ");
        try
        {
            double initialAmount = scan.nextDouble();
            if (scan.hasNextLine()) // Consume the leftover newline after a successful double input
            {
                scan.nextLine();
            }
            manager.createAccount(clientName,city,region,bankAccType,initialAmount,registry);
        }
        catch (InputMismatchException e)
        {
            System.out.println(" ");
            System.out.println("**Invalid input. Please enter a valid amount(in numbers) and try again.**");
            scan.nextLine(); // Consume the invalid text token so it doesn't break the next menu
        }

        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
    }

    private static void processBranchRegistration(Scanner scan,BranchRegistry registry)
    {

        if (scan.hasNextLine()) // Consumes any leftover newline from the main menu's scan.next()
        {
            scan.nextLine();
        }
        System.out.println(" ");
        System.out.println("----- Please enter the Branch details -----");
        System.out.println(" ");

        System.out.print("CITY : ");
        String city = scan.nextLine();
        city = normalizeString(city,"CITY",scan);

        System.out.print("REGION : ");
        String region = scan.nextLine();
        region = normalizeString(region,"REGION",scan);

        System.out.print("BRANCH CODE (4 digit code) : ");
        String branchCode = scan.next();
        branchCode = normalizeString(branchCode,"BRANCH CODE",scan);

        registry.addBranchCode(city,region,branchCode);

        boolean closeBranchRegistration = false;
        while(!closeBranchRegistration)
        {
            System.out.println(" ");
            System.out.print("Do you want to add more branches (y/n) : ");
            char choice = Character.toLowerCase(scan.next().charAt(0));
            switch (choice)
            {
                case 'n':

                    if (scan.hasNextLine())
                    {
                        scan.nextLine();
                    }
                    closeBranchRegistration=true;
                    break;

                case 'y':
                    if (scan.hasNextLine())
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("----- Please enter the Branch details -----");
                    System.out.println(" ");
                    System.out.print("CITY : ");
                    city = scan.nextLine();
                    city = normalizeString(city,"CITY",scan);

                    System.out.print("REGION : ");
                    region = scan.nextLine();
                    region = normalizeString(region,"REGION",scan);

                    System.out.print("BRANCH CODE (4 digit code) : ");
                    branchCode = scan.next();
                    branchCode = normalizeString(branchCode,"BRANCH CODE",scan);

                    registry.addBranchCode(city,region,branchCode);

                    break;

                default :
                    if (scan.hasNextLine())
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please enter a valid option-----");

            }

        }
        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
    }

    public static void authenticateAndShowMenu(Scanner scan,BranchRegistry registry,TransactionManager manager)
    {

        if (scan.hasNextLine()) // Consumes any leftover newline from the home page scan.next()
        {
            scan.nextLine();
        }
        char opt = 'y';
        boolean closeEmployeePortal=false;

        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
        System.out.println(" ");
        System.out.println("**Note** Use Employee ID = Admin and Password = samplepass123 for test purpose ***");
        System.out.println(" ");
        System.out.print("EMPLOYEE ID : ");
        String empId = scan.next();
        System.out.print("PASSWORD : ");
        String pass = scan.next();
        System.out.println(" ");

        while(!verifyEmployee(empId,pass))
        {
            if(opt=='y')
            {
                System.out.println("-----Employee Id or Password is Incorrect-----");
            }
            System.out.print("Do you want to continue and retry (y/n) : ");
            opt=Character.toLowerCase(scan.next().charAt(0));
            switch (opt)
            {
                case 'n':

                    if (scan.hasNextLine()) // Consumes any leftover newline from the opt's scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("----------------------------------------------------------------------");
                    return;

                case 'y':

                    if (scan.hasNextLine()) // Consumes any leftover newline from the opt's scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.print("EMPLOYEE ID : ");
                    empId = scan.next();
                    System.out.print("PASSWORD : ");
                    pass = scan.next();
                    System.out.println(" ");
                    break;

                default :
                    if (scan.hasNextLine()) // Consumes any leftover newline from the opt's scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please enter a valid option-----");

            }

        }

        System.out.println("----- Hi!, Please select your option ----- ");
        System.out.println(" ");
        System.out.println("1. REGISTER A BRANCH");
        System.out.println("2. OPEN AN ACCOUNT");
        System.out.println("3. Exit/Close ");
        System.out.println(" ");
        System.out.print("Enter your option number : ");

        while(!closeEmployeePortal)
        {

            char choice=scan.next().charAt(0);

            switch (choice)
            {
                case '1' :
                    processBranchRegistration(scan,registry);
                    System.out.println(" ");
                    System.out.println("---------- MAIN MENU ----------");
                    System.out.println(" ");
                    System.out.println("1. REGISTER A BRANCH");
                    System.out.println("2. OPEN AN ACCOUNT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;

                case '2' :
                    processAccountOpening(scan,registry,manager);
                    System.out.println(" ");
                    System.out.println("---------- MAIN MENU ----------");
                    System.out.println(" ");
                    System.out.println("1. REGISTER A BRANCH");
                    System.out.println("2. OPEN AN ACCOUNT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
                    break;

                case '3' :
                    if (scan.hasNextLine())
                    {
                        scan.nextLine();
                    }
                    closeEmployeePortal=true;
                    break;

                default :
                    if (scan.hasNextLine()) // Consumes any leftover newline from the main menu's scan.next()
                    {
                        scan.nextLine();
                    }
                    System.out.println(" ");
                    System.out.println("-----Please select a valid option-----");
                    System.out.println(" ");
                    System.out.println("1. REGISTER A BRANCH");
                    System.out.println("2. OPEN AN ACCOUNT");
                    System.out.println("3. Exit/Close ");
                    System.out.println(" ");
                    System.out.print("Enter your option number : ");
            }

        }
        System.out.println(" ");
        System.out.println("----------------------------------------------------------------------");
    }
}
