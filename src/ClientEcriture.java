import java.io.*;
import java.net.Socket;

public class ClientEcriture {

    private static final String IP_SERVEUR = "127.0.0.1";
    private static final int PORT_SERVEUR = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(ClientEcriture.IP_SERVEUR, ClientEcriture.PORT_SERVEUR)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("Bonjour");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}