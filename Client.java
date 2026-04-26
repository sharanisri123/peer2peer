import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static final String TRACKER_IP = "localhost";
    private static final int TRACKER_PORT = 5000;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your port (for peer server): ");
        int myPort = sc.nextInt();
        sc.nextLine();

        // Start peer server
        new Thread(() -> startPeerServer(myPort)).start();

        while (true) {
            System.out.println("\n1. Share File");
            System.out.println("2. Download File");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter file name: ");
                String filename = sc.nextLine();

                registerFile(filename, myPort);
            }

            else if (choice == 2) {
                System.out.print("Enter file name to download: ");
                String filename = sc.nextLine();

                searchAndDownload(filename);
            }

            else {
                break;
            }
        }

        sc.close();
    }

    // 🔹 Register file with tracker
    private static void registerFile(String filename, int port) throws Exception {
        Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);

        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println("REGISTER " + filename + " localhost " + port);

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(br.readLine());

        socket.close();
    }

    // 🔹 Search and download file
    private static void searchAndDownload(String filename) throws Exception {
        Socket socket = new Socket(TRACKER_IP, TRACKER_PORT);

        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println("SEARCH " + filename);

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String peerInfo = br.readLine();

        if (peerInfo == null || peerInfo.equals("NOTFOUND")) {
            System.out.println("File not found!");
            socket.close();
            return;
        }

        String[] parts = peerInfo.split(":");
        String peerIP = parts[0];
        int peerPort = Integer.parseInt(parts[1]);

        socket.close();

        downloadFile(peerIP, peerPort, filename);
    }

    // 🔹 Download from peer
    private static void downloadFile(String ip, int port, String filename) {
        try {
            Socket socket = new Socket(ip, port);

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(filename);

            InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream("downloaded_" + filename);

            byte[] buffer = new byte[4096];
            int bytes;

            while ((bytes = is.read(buffer)) > 0) {
                fos.write(buffer, 0, bytes);
            }

            System.out.println("Downloaded successfully!");

            fos.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Download failed!");
        }
    }

    // 🔹 Peer server to send files
    private static void startPeerServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Peer server running on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                new Thread(() -> {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String filename = br.readLine();

                        File file = new File(filename);

                        if (!file.exists()) {
                            socket.close();
                            return;
                        }

                        FileInputStream fis = new FileInputStream(file);
                        OutputStream os = socket.getOutputStream();

                        byte[] buffer = new byte[4096];
                        int bytes;

                        while ((bytes = fis.read(buffer)) > 0) {
                            os.write(buffer, 0, bytes);
                        }

                        fis.close();
                        socket.close();

                        System.out.println("File sent: " + filename);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}