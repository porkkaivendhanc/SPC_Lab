package exp10_log_monitoring_and_incident_management;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Alert {
        String source;
        String detail;

        Alert(String source, String detail) {
            this.source = source;
            this.detail = detail;
        }
    }

    public static void main(String[] args) {
        List<Alert> alerts = new ArrayList<>();
        alerts.add(new Alert("web-01", "Repeated login failures"));
        alerts.add(new Alert("db-01", "Suspicious query pattern"));

        System.out.println("Log Monitoring and Incident Management");
        System.out.println("======================================");
        for (Alert alert : alerts) {
            System.out.printf("Incident detected from %s: %s%n", alert.source, alert.detail);
        }
        System.out.println("Incident response workflow started.");
    }
}
