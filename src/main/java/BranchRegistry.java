import java.util.Map;
import java.util.HashMap;

public class BranchRegistry {
    // Outer Key: City Name (e.g., "Chennai")
    // Inner Key: Region Name (e.g., "Egmore")
    // Inner Value: Branch Routing Code (e.g., "1234")
    private Map<String, Map<String, String>> routingMap = new HashMap<>();

    // Method to store unique/distinct branch codes
    public void addBranchCode(String city, String region, String branchCode)
    {
        // Trim & convert to uppercase to prevent duplicates caused by typing differences and trailing space (e.g. "chennai" vs "Chennai")
        // we can also use .toLowerCase() if we want to maintain in lowercase
        String normalizedCity=city.trim().toUpperCase();
        String normalizedRegion=region.trim().toUpperCase();
        String normalizedCode=branchCode.trim().toUpperCase();

        // If the city doesn't exist, initialize its inner map
        if(!routingMap.containsKey(normalizedCity))
        {
            routingMap.put(normalizedCity,new HashMap<>());
        }
        // Getting the inner map for this specific city
        Map<String, String> regionMap = routingMap.get(normalizedCity);

        // Avoiding duplicates by verifying if the region or code already exist
        if(regionMap.containsKey(normalizedRegion))
        {
            System.out.println("Skipped: Region '" + region + "' already exists in " + city + ".");
            return;
        }
        if(regionMap.containsValue(normalizedCode))
        {
            System.out.println("Skipped: Branch Code '" + branchCode + "' is already assigned to another region.");
            return;
        }
        regionMap.put(normalizedRegion,normalizedCode);
        System.out.println("Successfully added branch: " + city + " (" + region + ") -> Code: " + branchCode);
    }

    // Method to return a specific branch routing code
    public String getBranchCode(String city, String region)
    {
        String normalizedCity=city.trim().toUpperCase();
        String normalizedRegion=region.trim().toUpperCase();
        if(routingMap.containsKey(normalizedCity))
        {
            Map<String, String> regionMap = routingMap.get(normalizedCity);
            if(regionMap.containsKey(normalizedRegion))
            {
                return regionMap.get(normalizedRegion);
            }
        }
        return "ERROR"; // if Branch code not found for city,region
    }
}
