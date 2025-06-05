import java.io.*;
import java.net.*;

public class Peer2 {
    private static final int PORT = 6000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Peer2 listening on port " + PORT);

        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message = in.readLine();
                    if (message != null) {
                        System.out.println("Peer1 says: " + message);
                    }
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while (true) {
            System.out.print("You (Peer2), type message (type 'exit' to quit): ");
            message = userInput.readLine();
            if ("exit".equalsIgnoreCase(message)) {
                System.out.println("Peer2 exiting...");
                break;
            }
            try (Socket socket = new Socket("localhost", 7000);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(message);
            } catch (IOException e) {
                System.out.println("Peer1 might not be ready yet.");
            }
        }
        serverSocket.close();
    }
}
