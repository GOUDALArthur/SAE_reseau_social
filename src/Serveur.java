import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 8080;
    private static BDServeur bd = new BDServeur();

    public static BDServeur getBd() {
        return bd;
    }

    public static void main(String[] args) {
        try (ServerSocket socketServer = new ServerSocket(Serveur.PORT)) {
            while (true) {
                Socket socketClient = socketServer.accept();
                System.out.println("Connexion d'un client");
                String pseudo = authentifieClient(socketClient);

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
        String pseudo = reader.readLine();
        System.out.println("Authentification de " + pseudo + " en cours");
        Utilisateur utilisateur = Serveur.bd.getUtilisateur(pseudo);

        if (utilisateur == null) {
            writer.println("inexistant");
            writer.println("Compte inexistant\nVoulez-vous créer un compte ? (O/N) ");
            if (reader.readLine().equals("O")) {
                System.out.println("Création du compte de " + pseudo);
                Serveur.bd.addUtilisateur(pseudo);
                System.out.println(Serveur.bd.getUtilisateurs());
                System.out.println(Serveur.bd.getUtilisateur(pseudo));
            } else {
                writer.println("echec");
                return null;
            }
        }
        writer.println("reussite");
        return pseudo;
    }

}
