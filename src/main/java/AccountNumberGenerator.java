import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class AccountNumberGenerator {

    // For the unique 9 digit generation logic
    private int currentPrefix = 0; // Starts at 00000
    private Set<Integer> usedSuffixesInCurrentBlock = new HashSet<>();
    private Random random = new Random();
    private final int SUFFIX_START = 1001;
    private final int SUFFIX_END = 9999;
    private final int TOTAL_SUFFIXES_PER_BLOCK = 8999; // (9999 - 1001) + 1

    private static String getAccountTypeCode(String type)
    {
        return switch (type) {
            case "current" -> "20";
            case "business" -> "30";
            default -> "10"; // for savings account as default
        };
    }

    // For generating a unique 9 digit sequential/random identification block
    private String generateUniqueNineDigitBlock()
    {
        // If all 8,999 numbers in the current suffix block are exhausted, increment the prefix
        if(usedSuffixesInCurrentBlock.size() >= TOTAL_SUFFIXES_PER_BLOCK)
        {
            currentPrefix++;
            usedSuffixesInCurrentBlock.clear(); // Resetting the suffix tracking for the new prefix block
        }

        // For generating a random 4-digit number between 1001 and 9999 until we find one not used in this block
        int suffix = 0;
        while (true) {
            suffix = random.nextInt((SUFFIX_END - SUFFIX_START) + 1) + SUFFIX_START;
            if (!usedSuffixesInCurrentBlock.contains(suffix)) {
                usedSuffixesInCurrentBlock.add(suffix);
                break;
            }
        }

        // %05d ensures the prefix is exactly 5 digits
        String prefixStr=String.format("%05d",currentPrefix);
        String suffixStr=String.valueOf(suffix);
        return prefixStr+suffixStr;
    }

    public String generateAccountNumber(String city, String bankRegion, String accountType)
    {
        // To fetch the Branch code
        BranchRegistry registry = new BranchRegistry();
        String branchCode = registry.getBranchCode(city,bankRegion);
        if(branchCode.equalsIgnoreCase("ERROR"))
        {
            System.out.println("ERROR: Branch code not found for " + city + " (" + bankRegion + ")");
            return null; // Returned null to indicate generation failed
        }

        StringBuilder uniqueAccountNumber = new StringBuilder();
        uniqueAccountNumber.append(branchCode);
        uniqueAccountNumber.append(getAccountTypeCode(accountType)); // fetching account type code
        return uniqueAccountNumber.toString();
    }
}
