import org.cloudbus.cloudsim.core.CloudSim;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SecureFileSharingSimulation {

    static class User {
        String username;
        String password;
        boolean authenticated;

        User(String username, String password) {
            this.username = username;
            this.password = password;
            this.authenticated = false;
        }
    }

    static class FileRequest {
        String fileName;
        int size;

        FileRequest(String fileName, int size) {
            this.fileName = fileName;
            this.size = size;
        }
    }

    static Map<String, byte[]> cloudStorage =
            new HashMap<String, byte[]>();

    static Map<String, SecretKey> encryptionKeys =
            new HashMap<String, SecretKey>();

    static int successfulUploads = 0;
    static int successfulDownloads = 0;

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

            List<User> users = createUsers();

            List<FileRequest> requests =
                    generateFileRequests();

            long totalUploadTime = 0;
            long totalDownloadTime = 0;

            System.out.println();
            System.out.println(
                    "=============================================="
            );
            System.out.println(
                    "       SECURE FILE SHARING USING CLOUDSIM"
            );
            System.out.println(
                    "=============================================="
            );

            for (FileRequest request : requests) {

                User user =
                        selectUser(users);

                byte[] fileData =
                        generateFileData(request.size);

                long uploadStart =
                        System.nanoTime();

                boolean uploadResult =
                        uploadFile(
                                user,
                                request.fileName,
                                fileData
                        );

                long uploadEnd =
                        System.nanoTime();

                totalUploadTime +=
                        uploadEnd - uploadStart;

                if (uploadResult) {

                    System.out.println(
                            "Upload Successful : "
                                    + request.fileName
                    );

                    long downloadStart =
                            System.nanoTime();

                    byte[] downloadedData =
                            downloadFile(
                                    user,
                                    request.fileName
                            );

                    long downloadEnd =
                            System.nanoTime();

                    totalDownloadTime +=
                            downloadEnd - downloadStart;

                    if (downloadedData != null) {

                        successfulDownloads++;

                        System.out.println(
                                "Download Successful : "
                                        + request.fileName
                        );
                    }
                }

                System.out.println(
                        "----------------------------------------------"
                );
            }

            printResults(
                    requests,
                    totalUploadTime,
                    totalDownloadTime
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static List<User> createUsers() {

        List<User> users =
                new ArrayList<User>();

        users.add(
                new User(
                        "admin",
                        "admin123"
                )
        );

        users.add(
                new User(
                        "user1",
                        "user123"
                )
        );

        return users;
    }

    private static User selectUser(
            List<User> users) {

        return users.get(1);
    }

    private static List<FileRequest>
    generateFileRequests() {

        List<FileRequest> requests =
                new ArrayList<FileRequest>();

        requests.add(
                new FileRequest(
                        "document.txt",
                        1024
                )
        );

        requests.add(
                new FileRequest(
                        "report.txt",
                        2048
                )
        );

        requests.add(
                new FileRequest(
                        "cloud_data.txt",
                        4096
                )
        );

        return requests;
    }

    private static byte[] generateFileData(
            int size) {

        byte[] data =
                new byte[size];

        new Random().nextBytes(data);

        return data;
    }

    private static boolean authenticate(
            User user) {

        if (
                user.username.equals("user1")
                        &&
                user.password.equals("user123")
        ) {

            user.authenticated = true;

            return true;
        }

        return false;
    }

    private static boolean uploadFile(
            User user,
            String filename,
            byte[] fileData) {

        if (!authenticate(user)) {

            System.out.println(
                    "Authentication Failed"
            );

            return false;
        }

        try {

            KeyGenerator keyGenerator =
                    KeyGenerator.getInstance("AES");

            keyGenerator.init(128);

            SecretKey key =
                    keyGenerator.generateKey();

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key
            );

            byte[] encryptedData =
                    cipher.doFinal(fileData);

            cloudStorage.put(
                    filename,
                    encryptedData
            );

            encryptionKeys.put(
                    filename,
                    key
            );

            successfulUploads++;

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private static byte[] downloadFile(
            User user,
            String filename) {

        if (!user.authenticated) {

            System.out.println(
                    "Access Denied"
            );

            return null;
        }

        try {

            byte[] encryptedData =
                    cloudStorage.get(filename);

            SecretKey key =
                    encryptionKeys.get(filename);

            if (
                    encryptedData == null
                            ||
                    key == null
            ) {

                return null;
            }

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    key
            );

            return cipher.doFinal(
                    encryptedData
            );

        } catch (Exception e) {

            return null;
        }
    }

    private static void printResults(
            List<FileRequest> requests,
            long totalUploadTime,
            long totalDownloadTime) {

        double uploadTime =
                totalUploadTime / 1_000_000.0;

        double downloadTime =
                totalDownloadTime / 1_000_000.0;

        double totalTime =
                uploadTime + downloadTime;

        double throughput =
                totalTime > 0
                        ? requests.size() /
                        (totalTime / 1000.0)
                        : 0;

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "             SIMULATION RESULTS"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "Total File Requests : "
                        + requests.size()
        );

        System.out.println(
                "Successful Uploads : "
                        + successfulUploads
        );

        System.out.println(
                "Successful Downloads : "
                        + successfulDownloads
        );

        System.out.println(
                "Encrypted Files : "
                        + cloudStorage.size()
        );

        System.out.printf(
                "Upload Time : %.3f ms%n",
                uploadTime
        );

        System.out.printf(
                "Download Time : %.3f ms%n",
                downloadTime
        );

        System.out.printf(
                "Total Response Time : %.3f ms%n",
                totalTime
        );

        System.out.printf(
                "Throughput : %.3f requests/sec%n",
                throughput
        );

        System.out.println(
                "Security : AES Encryption"
        );

        System.out.println(
                "Authentication : Enabled"
        );

        System.out.println(
                "Access Control : Enabled"
        );

        System.out.println(
                "=============================================="
        );
    }
}