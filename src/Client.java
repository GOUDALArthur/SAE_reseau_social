import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String IP_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 8080;

    public static void main(String[] args) {
        BDServeur bd = Serveur.getBd();
        try (Socket socket = new Socket(Client.IP_SERVEUR, Client.PORT_SERVEUR)) {
            System.out.println("Connexion au serveur " + IP_SERVEUR + " sur le port " + PORT_SERVEUR);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine());
            String pseudo = scanner.nextLine();
            writer.println(pseudo);
            
            String reponseServeur = reader.readLine();
            if (reponseServeur.equals("inexistant")) {
                System.out.println(reader.readLine() + "\n" + reader.readLine());
                writer.println(scanner.nextLine());
            }
            reponseServeur = reader.readLine();
            System.out.println(reponseServeur);
            if (reponseServeur.equals("reussite")) {
                System.out.println(bd.getUtilisateurs());
                Utilisateur utilisateur = bd.getUtilisateur(pseudo);
                utilisateur.setSocket(socket);
                utilisateur.connexion();
                new Thread(bd.getUtilisateur(pseudo)).start();
            }

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