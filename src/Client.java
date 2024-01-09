import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String IP_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(Client.IP_SERVEUR, Client.PORT_SERVEUR)) {
            System.out.println("Connexion au serveur " + IP_SERVEUR + " sur le port " + PORT_SERVEUR + "\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine());
            String pseudo = scanner.nextLine();
            writer.println(pseudo); writer.flush();
            // Authentification --> Serveur.authentifieClient()
            String reponseServeur = reader.readLine();

            if (reponseServeur.equals("inexistant")) {
                System.out.println(reader.readLine() + "\n" + reader.readLine());
                writer.println(scanner.nextLine());
                reponseServeur = reader.readLine();
            }
            
            if (reponseServeur.equals("reussite")) {
                System.out.println(reader.readLine());
                System.out.print("Envoyer un message : ");
                String message = scanner.nextLine();
                while (message != null) {
                    writer.println(message); writer.flush();
                    System.out.print("Envoyer un message : ");
                    message = scanner.nextLine();
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}