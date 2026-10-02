import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BranchRegistry {

    // Method to store unique/distinct branch codes
    public void addBranchCode(String city, String region, String branchCode)
    {
        // Trim & convert to uppercase to prevent duplicates caused by typing differences and trailing space (e.g. "chennai" vs "Chennai")
        // we can also use .toLowerCase() if we want to maintain in lowercase
        String normalizedCity=city.trim().toUpperCase();
        String normalizedRegion=region.trim().toUpperCase();
        String normalizedCode=branchCode.trim().toUpperCase();

        // To prevent from SQL Injection vulnerabilities, Parameterized SQL query is used.
        String sql="INSERT INTO branch_registry (city,region,branch_code) VALUES (?,?,?)";

        try(Connection conn=DatabaseConfig.getConnection(); PreparedStatement pstmt=conn.prepareStatement(sql))
        {
            // Securely bind values into placeholders
            pstmt.setString(1,normalizedCity);
            pstmt.setString(2,normalizedRegion);
            pstmt.setString(3,normalizedCode);
            // Execute/Run the fully completed query
            pstmt.executeUpdate();
            System.out.println("Successfully added branch: " + city + " (" + region + ") -> Code: " + branchCode);
        } catch (SQLException e)
        {
            // Checking why error occurred : Is it due to duplicate (city,region) pair / branch code
            // Or is it due to Database Access Error.
            if(e.getErrorCode() == 1062)
            {
                String errorMessage=e.getMessage().toUpperCase();
                if(errorMessage.contains("PRIMARY"))
                {
                    System.out.println("Skipped: Region '" + region + "' already exists in " + city + ".");
                }
                else if(errorMessage.contains("UNIQUE_BRANCH_CODE"))
                {
                    System.out.println("Skipped: Branch Code '" + branchCode + "' is already assigned to another region.");
                }
                else
                {
                    System.out.println("Skipped: Duplicate branch mapping constraint triggered.");
                }
            }
            else
            {
                System.out.println("Database Access Error during Insertion : "+e.getMessage());
            }
        }

    }

    // Method to return a specific branch routing code
    public String getBranchCode(String city, String region)
    {
        String normalizedCity=city.trim().toUpperCase();
        String normalizedRegion=region.trim().toUpperCase();

        String sql="SELECT branch_code FROM branch_registry WHERE city = ? AND region = ?";

        try(Connection conn=DatabaseConfig.getConnection();PreparedStatement pstmt=conn.prepareStatement(sql))
        {
            pstmt.setString(1,normalizedCity);
            pstmt.setString(2,normalizedRegion);
            try(ResultSet rs=pstmt.executeQuery())
            {
                if(rs.next())
                {
                    return rs.getString("branch_code");
                }
            }

        } catch (SQLException e)
        {
            System.out.println("Database Execution Exception during branch lookup: " + e.getMessage());
        }

        // If in case there is an error/unknown city/region listed in the list of select city,select region options
        return "ERROR"; // if Branch code not found for city,region
    }
}
