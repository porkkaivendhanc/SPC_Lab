import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.*;

public class LogForensicsSimulation {

    static class LogEntry {

        String timestamp;
        String sourceIP;
        String destinationIP;
        String message;

        LogEntry(
                String timestamp,
                String sourceIP,
                String destinationIP,
                String message) {

            this.timestamp = timestamp;
            this.sourceIP = sourceIP;
            this.destinationIP = destinationIP;
            this.message = message;
        }
    }

    public static void main(String[] args) {

        try {

            int numUsers = 1;

            Calendar calendar =
                    Calendar.getInstance();

            CloudSim.init(
                    numUsers,
                    calendar,
                    false
            );

            List<LogEntry> logData =
                    generateLogData();

            List<LogEntry> suspiciousActivities =
                    detectSuspiciousActivities(logData);

            List<LogEntry> anomalies =
                    detectAnomalies(logData);

            printSuspiciousActivities(
                    suspiciousActivities
            );

            printAnomalies(anomalies);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static List<LogEntry> generateLogData() {

        List<LogEntry> logs =
                new ArrayList<LogEntry>();

        logs.add(
                new LogEntry(
                        "2023-06-01 10:23:45",
                        "192.168.1.100",
                        "203.0.113.10",
                        "Unauthorized access attempt."
                )
        );

        logs.add(
                new LogEntry(
                        "2023-06-02 14:55:12",
                        "192.168.1.150",
                        "203.0.113.20",
                        "High volume of outbound traffic to suspicious IP address."
                )
        );

        logs.add(
                new LogEntry(
                        "2023-06-03 09:10:27",
                        "192.168.1.200",
                        "203.0.113.30",
                        "Unusual login activity from multiple IP addresses."
                )
        );

        logs.add(
                new LogEntry(
                        "2023-06-01 12:05:30",
                        "192.168.1.75",
                        "203.0.113.10",
                        "Abnormal CPU utilization exceeding threshold."
                )
        );

        logs.add(
                new LogEntry(
                        "2023-06-02 16:30:15",
                        "192.168.1.110",
                        "203.0.113.20",
                        "Unusually large file transfer size."
                )
        );

        logs.add(
                new LogEntry(
                        "2023-06-03 11:40:21",
                        "192.168.1.180",
                        "203.0.113.30",
                        "Unusual memory consumption pattern."
                )
        );

        return logs;
    }

    private static List<LogEntry>
    detectSuspiciousActivities(
            List<LogEntry> logData) {

        List<LogEntry> suspicious =
                new ArrayList<LogEntry>();

        for (LogEntry log : logData) {

            String message =
                    log.message.toLowerCase();

            if (
                    message.contains("unauthorized")
                            ||
                    message.contains("suspicious")
                            ||
                    message.contains("unusual login")
            ) {

                suspicious.add(log);
            }
        }

        return suspicious;
    }

    private static List<LogEntry>
    detectAnomalies(
            List<LogEntry> logData) {

        List<LogEntry> anomalies =
                new ArrayList<LogEntry>();

        for (LogEntry log : logData) {

            String message =
                    log.message.toLowerCase();

            if (
                    message.contains("cpu")
                            ||
                    message.contains("file transfer")
                            ||
                    message.contains("memory")
            ) {

                anomalies.add(log);
            }
        }

        return anomalies;
    }

    private static void printSuspiciousActivities(
            List<LogEntry> suspiciousActivities) {

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       DETECTED SUSPICIOUS ACTIVITIES"
        );

        System.out.println(
                "=============================================="
        );

        int count = 1;

        for (LogEntry log : suspiciousActivities) {

            System.out.println(
                    count + ". Timestamp: "
                            + log.timestamp
            );

            System.out.println(
                    "   Source IP: "
                            + log.sourceIP
            );

            System.out.println(
                    "   Destination IP: "
                            + log.destinationIP
            );

            System.out.println(
                    "   Log Message: "
                            + log.message
            );

            System.out.println();

            count++;
        }
    }

    private static void printAnomalies(
            List<LogEntry> anomalies) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "             DETECTED ANOMALIES"
        );

        System.out.println(
                "=============================================="
        );

        int count = 1;

        for (LogEntry log : anomalies) {

            System.out.println(
                    count + ". Timestamp: "
                            + log.timestamp
            );

            System.out.println(
                    "   Source IP: "
                            + log.sourceIP
            );

            System.out.println(
                    "   Destination IP: "
                            + log.destinationIP
            );

            System.out.println(
                    "   Log Message: "
                            + log.message
            );

            System.out.println();

            count++;
        }
    }
}