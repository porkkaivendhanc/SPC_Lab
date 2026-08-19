package exp09_attribute_based_access_control;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("department", "finance");
        attributes.put("clearance", "high");

        String department = attributes.get("department");
        String clearance = attributes.get("clearance");

        System.out.println("Attribute-Based Access Control");
        System.out.println("=============================");
        if (department.equals("finance") && clearance.equals("high")) {
            System.out.println("Access granted based on attributes.");
        } else {
            System.out.println("Access denied.");
        }
    }
}
