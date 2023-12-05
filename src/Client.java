import java.io.*;
import java.net.Socket;

public class Client {

    private static final String IP_SERVEUR = "127.0.0.1";
    private static final int PORT_SERVEUR = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(Client.IP_SERVEUR, Client.PORT_SERVEUR)) {
            System.out.println("Connexion au serveur " + IP_SERVEUR + " sur le port " + PORT_SERVEUR);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            String message = reader.readLine();
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}