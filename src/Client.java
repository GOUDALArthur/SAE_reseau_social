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
                // Création d'un thread pour recevoir et afficher les réponses du serveur en permanence
                Thread lecteurServeur = new Thread(() -> {
                    String reponse;
                    try {
                        while ((reponse = reader.readLine()) != null) {
                            if (reponse.equals("\n") || reponse.startsWith("("))
                                System.out.println(reponse);
                            else if (!reponse.equals(""))
                                System.out.println("[SERVEUR] : " + reponse);
                        }
                    } catch (IOException e) {
                        System.out.println("Erreur de lecture depuis le serveur : " + e.getMessage());
                    }
                });
                lecteurServeur.start();
                Thread.sleep(1000);

                String message = scanner.nextLine();
                while (message != null) {
                    writer.println(message); writer.flush();
                    message = scanner.nextLine();
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}