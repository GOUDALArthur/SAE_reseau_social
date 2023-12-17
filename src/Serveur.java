import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket socketServer = new ServerSocket(Serveur.PORT)) {
            BDServeur.load();
            while (true) {
                Socket socketClient = socketServer.accept();
                System.out.println("Connexion d'un client");
                String pseudo = authentifieClient(socketClient);

                Utilisateur utilisateur = BDServeur.getUtilisateur(pseudo);
                utilisateur.setSocket(socketClient);
                utilisateur.connexion();
                System.out.println("Connexion de " + pseudo + " réussie");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static String authentifieClient(Socket socketClient) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter writer = new PrintWriter(socketClient.getOutputStream(), true);
        
        writer.println("Entrer votre nom d'utilisateur : ");
        System.out.println("Attente du nom d'utilisateur...");
        String pseudo = reader.readLine();
        System.out.println("Nom d'utilisateur reçu : " + pseudo);
        System.out.println("Authentification de " + pseudo + " en cours");
        Utilisateur utilisateur = BDServeur.getUtilisateur(pseudo);

        if (utilisateur == null) {
            writer.println("Compte inexistant\nVoulez-vous créer un compte ? (O/N) ");
            String reponse = reader.readLine();
            System.out.println("Réponse du client : " + reponse);
            if (reader.readLine().equals("O")) {
                BDServeur.addUtilisateur(pseudo);
            } else {
                return null;
            }
        }
        return pseudo;
    }

}
