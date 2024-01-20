import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Serveur {

    /**
     * Port d'écoute du serveur.
     */
    private static final int PORT = 8080;

    /**
     * Map pour stocker les gestionnaires de clients connectés.
     */
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<String, ClientHandler>();

    /**
     * Méthode principale du serveur.
     *
     */
    public static void main(String[] args) {
        try (ServerSocket socketServer = new ServerSocket(Serveur.PORT)) {
            BDServeur bd = BDServeur.getInstance();
            bd.load();
            Scanner scanner = new Scanner(System.in);

            // Thread pour lire les commandes serveur depuis la console.
            Thread lecteurCommandes = new Thread(() -> {
                String commandeServeur = scanner.nextLine();
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
                        case "shutdown":
                            System.out.println("Arrêt du serveur...");
                            scanner.close();
                            bd.save();
                            break;
                        default:
                            System.out.println("Commande inconnue");
                            break;
                    }
                }
            });
            lecteurCommandes.start();

            // Boucle principale pour accepter les connexions des clients.
            while (true) {
                Socket socketClient = socketServer.accept();
                Thread authentificateur = new Thread(() -> {
                    try {
                        String pseudo = Serveur.authentifieClient(socketClient);
                        Utilisateur util = new Utilisateur(pseudo);
                        ClientHandler clientHandler = new ClientHandler(socketClient, util);
                        new Thread(clientHandler).start();
                        Serveur.clients.put(pseudo, clientHandler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                authentificateur.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient le gestionnaire de client pour un pseudo donné.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Le gestionnaire de client associé au pseudo.
     */
    public static ClientHandler getClientHandler(String pseudo) {
        return Serveur.clients.get(pseudo);
    }

    /**
     * Authentifie un client lors de sa connexion.
     *
     * @param socketClient La socket du client.
     * @return Le pseudo de l'utilisateur authentifié.
     * @throws IOException En cas d'erreur.
     */
    private static String authentifieClient(Socket socketClient) throws IOException {
        BDServeur bd = BDServeur.getInstance();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        PrintWriter writer = new PrintWriter(socketClient.getOutputStream(), true);

        writer.println("Entrer votre nom d'utilisateur : ");
        String pseudo = reader.readLine();
        Utilisateur utilisateur = bd.getUtilisateur(pseudo);

        if (utilisateur == null) {
            writer.println("inexistant");
            writer.println("Compte inexistant\nVoulez-vous créer un compte ? (O/N)");
            String choix = reader.readLine();
            if (choix.equals("O") || choix.equals("o")) {
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

    /**
     * Supprime un message.
     *
     * @param commande La commande serveur.
     */
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

    /**
     * Supprime un utilisateur.
     *
     * @param commande La commande serveur.
     */
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
