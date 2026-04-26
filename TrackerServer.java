import java.io.*;
import java.net.*;
import java.util.*;

public class TrackerServer {
    private static Map<String, List<String>> fileMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Tracker Server started on port 5000...");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            String request = br.readLine();
            String[] parts = request.split(" ");

            if (parts[0].equals("REGISTER")) {
                String filename = parts[1];
                String peer = parts[2] + ":" + parts[3];

                fileMap.putIfAbsent(filename, new ArrayList<>());
                fileMap.get(filename).add(peer);

                pw.println("REGISTERED");
                System.out.println("File registered: " + filename + " from " + peer);
            }

            else if (parts[0].equals("SEARCH")) {
                String filename = parts[1];

                if (fileMap.containsKey(filename)) {
                    for (String peer : fileMap.get(filename)) {
                        pw.println(peer);
                    }
                } else {
                    pw.println("NOTFOUND");
                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}