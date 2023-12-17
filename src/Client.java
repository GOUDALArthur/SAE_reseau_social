import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String IP_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(Client.IP_SERVEUR, Client.PORT_SERVEUR)) {
            System.out.println("Connexion au serveur " + IP_SERVEUR + " sur le port " + PORT_SERVEUR);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine());
            String pseudo = scanner.nextLine();
            System.out.println("Envoie du nom " + pseudo);
            writer.println(pseudo);

            String message = reader.readLine();
            while (message != null) {
                System.out.println(message);
                message = reader.readLine();
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}