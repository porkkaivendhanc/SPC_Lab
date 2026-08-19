package exp08_role_based_access_control;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> roles = new HashMap<>();
        roles.put("admin", "full");
        roles.put("user", "read");

        String user = "admin";
        String action = "delete";

        System.out.println("Role-Based Access Control");
        System.out.println("=========================");
        String rolePermission = roles.getOrDefault(user, "none");

        if (rolePermission.equals("full") || action.equals("read")) {
            System.out.println("Access granted for " + action + " action.");
        } else {
            System.out.println("Access denied.");
        }
    }
}
