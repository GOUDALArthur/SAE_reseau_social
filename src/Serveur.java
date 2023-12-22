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
                //String pseudo = authentifieClient(socketClient);
                String pseudo = demandePseudo(socketClient);
                Utilisateur util = new Utilisateur(pseudo);
                ClientHandler clientHandler = new ClientHandler(socketClient, util);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static String demandePseudo(Socket socketClient) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter writer = new PrintWriter(socketClient.getOutputStream(), true);
        
        writer.println("Entrer votre nom d'utilisateur : ");
        System.out.println("Attente du nom d'utilisateur...");
        String pseudo = reader.readLine();
        System.out.println("Nom d'utilisateur reçu : " + pseudo);
        return pseudo;
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
            writer.println("inexistant");
            writer.println("Compte inexistant\nVoulez-vous créer un compte ? (O/N) ");
            if (reader.readLine().equals("O")) {
                System.out.println("Création du compte de " + pseudo);
                BDServeur.addUtilisateur(pseudo);
            } else {
                writer.println("echec");
                return null;
            }
        }
        writer.println("reussite");
        return pseudo;
    }

}
