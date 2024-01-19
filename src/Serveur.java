import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket socketServer = new ServerSocket(Serveur.PORT)) {
            BDServeur bd = BDServeur.getInstance();
            bd.load();
            Scanner scanner = new Scanner(System.in);
            String commandeServeur = "";
            while (!commandeServeur.equals("/shutdown")) {
                Socket socketClient = socketServer.accept();
                Thread authentificateur = new Thread(() -> {
                    try {
                        System.out.println("Connexion d'un client");
                        String pseudo = Serveur.authentifieClient(socketClient);
                        Utilisateur util = new Utilisateur(pseudo);
                        ClientHandler clientHandler = new ClientHandler(socketClient, util);
                        new Thread(clientHandler).start();
                        System.out.println("Client connecté : " + pseudo);
                    } catch (IOException e) {
                        System.out.println("Erreur de lecture depuis le serveur : " + e.getMessage());
                    }
                });
                authentificateur.start();

                commandeServeur = scanner.nextLine();
                if (commandeServeur.startsWith("/")) {
                    String[] commande = commandeServeur.split(" ");
                    commande[0] = commande[0].substring(1);
                    switch (commande[0]) {
                        case "delete":
                            Serveur.deleteMessage(commande);
                            break;
                        case "remove":
                            Serveur.removeUtilisateur(commande);
                            break;
                        default:
                            System.out.println("Commande inconnue");
                            break;
                    }
                }
            }
            scanner.close();
            bd.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String authentifieClient(Socket socketClient) throws IOException {
        BDServeur bd = BDServeur.getInstance();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter writer = new PrintWriter(socketClient.getOutputStream(), true);
        
        writer.println("Entrer votre nom d'utilisateur : ");
        System.out.println("Attente du nom d'utilisateur...");
        String pseudo = reader.readLine();
        System.out.println("Authentification de " + pseudo + " en cours");
        Utilisateur utilisateur = bd.getUtilisateur(pseudo);

        if (utilisateur == null) {
            writer.println("inexistant");
            writer.println("Compte inexistant\nVoulez-vous créer un compte ? (O/N)");
            String choix = reader.readLine();
            if (choix.equals("O") || choix.equals("o")) {
                System.out.println("Création du compte de " + pseudo);
                bd.addUtilisateur(pseudo);
                utilisateur = bd.getUtilisateur(pseudo);
            } else {
                writer.println("echec");
                return null;
            }
        }
        utilisateur.connexion();
        writer.println("reussite");
        return pseudo;
    }

    private static void deleteMessage(String[] commande) {
        BDServeur bd = BDServeur.getInstance();
        int idMessage;
        try {
            idMessage = Integer.parseInt(commande[1]);
            Message message = bd.getMessage(idMessage);
            if (message != null) {
                bd.deleteMessage(idMessage);
                System.out.println("Message supprimé");
            } else {
                System.out.println("Ce message n'existe pas");
            }

        } catch (NumberFormatException e) {
            System.out.println("L'id du message doit être un entier");
        }
    }

    private static void removeUtilisateur(String[] commande) {
        BDServeur bd = BDServeur.getInstance();
        String pseudo = commande[1];
        Utilisateur utilisateur = bd.getUtilisateur(pseudo);
        if (utilisateur != null) {
            bd.removeUtilisateur(pseudo);
            System.out.println("Utilisateur supprimé");
        } else {
            System.out.println("Cet utilisateur n'existe pas");
        }
    }

}
