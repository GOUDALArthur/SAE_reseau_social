import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientHandler implements Runnable {

    /**
     * Socket du client.
     */
    private Socket socketClient;

    /**
     * Lecteur des messages de la socket.
     */
    private BufferedReader reader;

    /**
     * Écrivain des messages sur la socket.
     */
    private PrintWriter writer;

    /**
     * Utilisateur associé au client.
     */
    private Utilisateur utilisateur;

    /**
     * Instance de la base de données serveur.
     */
    private BDServeur bd = BDServeur.getInstance();

    /**
     * Constructeur de la classe.
     *
     * @param socketClient Socket du client.
     * @param utilisateur Utilisateur associé au client.
     */
    public ClientHandler(Socket socketClient, Utilisateur utilisateur) {
        this.socketClient = socketClient;
        this.utilisateur = utilisateur;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            this.writer = new PrintWriter(socketClient.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode exécutée dans le thread.
     */
    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            String bienvenue = "Bienvenue, " + this.utilisateur.getPseudo() + " ! On est encore en test mais tkt ça arrive fort !";
            String intro = "Suivez vos amis avec /follow ! Envoyez des messages en appuyant sur Entrée ! (Utilisez /help)";
            writer.println(bienvenue + "\n" + intro);
            writer.flush();

            String message = this.reader.readLine();
            while (message != null) {
                if (message.startsWith("/")) {
                    String[] commande = message.split(" ");
                    commande[0] = commande[0].substring(1);
                    switch (commande[0]) {
                        case "help":
                            writer.println("/follow <pseudo> : s'abonner à un utilisateur\n" +
                                           "/unfollow <pseudo> : se désabonner d'un utilisateur\n" +
                                           "/like <id_message> : liker un message\n" +
                                           "/dislike <id_message> : disliker un message\n" +
                                           "/delete <id_message> : supprimer un de ses messages\n");
                            break;
                        case "follow":
                            writer.println(this.follow(commande));
                            writer.flush();
                            break;
                        case "unfollow":
                            writer.println(this.unfollow(commande));
                            writer.flush();
                            break;
                        case "like":
                            writer.println(this.like(commande));
                            writer.flush();
                            break;
                        case "dislike":
                            writer.println(this.dislike(commande));
                            writer.flush();
                            break;
                        case "delete":
                            writer.println(this.deleteMessage(commande));
                            writer.flush();
                            break;
                        default:
                            writer.println("Commande inconnue");
                            writer.flush();
                            break;
                    }
                } else {
                    this.envoieMessage(message);
                }
                message = this.reader.readLine();
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.reader.close();
                this.writer.close();
                this.socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.utilisateur.deconnexion();
            }
        }
    }

    /**
     * Donne l'écrivain de la socket pour l'envoi de messages au client.
     *
     * @return L'écrivain de la socket.
     */
    public PrintWriter getWriter() {
        return this.writer;
    }

    /**
     * Méthode pour suivre un utilisateur.
     *
     * @param commande Les éléments de la commande client.
     * @return Le résultat de l'opération.
     */
    private String follow(String[] commande) {
        if (commande.length < 2)
            return "Erreur --> Préciser le pseudo de l'utilisateur à suivre";
        if (this.bd.getUtilisateur(commande[1]) == null || !bd.getUtilisateurs().containsKey(commande[1]))
            return "Erreur --> Utilisateur inexistant";
        if (commande[1].equals(this.utilisateur.getPseudo()))
            return "Erreur --> Vous ne pouvez pas vous suivre vous-même (trouvez-vous des amis)";
        if (this.bd.getFollowers().containsKey(commande[1]) && bd.getFollowers().get(commande[1]).contains(this.utilisateur.getPseudo()))
            return "Erreur --> Vous suivez déjà cet utilisateur";
        this.bd.addFollower(commande[1], utilisateur.getPseudo());
        return "Vous suivez désormais " + commande[1];
    }

    /**
     * Méthode pour ne plus suivre un utilisateur.
     *
     * @param commande Les éléments de la commande client.
     * @return Le résultat de l'opération.
     */
    private String unfollow(String[] commande) {
        if (commande.length < 2)
            return "Erreur --> Préciser le pseudo de l'utilisateur à ne plus suivre";
        if (this.bd.getUtilisateur(commande[1]) == null || !bd.getUtilisateurs().containsKey(commande[1]))
            return "Erreur --> Utilisateur inexistant";
        if (commande[1].equals(this.utilisateur.getPseudo()))
            return "Erreur --> Vous ne pouvez pas vous désabonner de vous-même (réflechissez un peu)";
        if (this.bd.getFollowers().containsKey(commande[1]) && !bd.getFollowers().get(commande[1]).contains(this.utilisateur.getPseudo()))
            return "Erreur --> Vous ne suivez pas cet utilisateur";
        this.bd.delFollower(commande[1], utilisateur.getPseudo());
        return "Vous ne suivez désormais plus " + commande[1];
    }

    /**
     * Méthode pour aimer un message.
     *
     * @param commande Les éléments de la commande client.
     * @return Le résultat de l'opération.
     */
    private String like(String[] commande) {
        if (commande.length < 2)
            return "Erreur --> Préciser l'id du message";
        int idMessage;
        try {
            idMessage = Integer.parseInt(commande[1]);
        } catch (NumberFormatException e) {
            return "Erreur --> L'id du message doit être un entier";
        }
        if (this.bd.getMessage(idMessage) == null)
            return "Erreur --> Message inexistant";
        Message message = this.bd.getMessage(idMessage);
        if (message.getLikes().contains(this.utilisateur)) {
            return "Erreur --> Vous avez déjà liké ce message";
        }
        message.addLike(this.utilisateur);
        return "Vous avez liké le message " + idMessage;
    }
/**
 * Méthode pour ne plus aimer un message.
 *
 * @param commande Les éléments de la commande client.
 * @return Le résultat de l'opération.
 */
private String dislike(String[] commande) {
    if (commande.length < 2)
        return "Erreur --> Préciser l'id du message";
    int idMessage;
    try {
        idMessage = Integer.parseInt(commande[1]);
    } catch (NumberFormatException e) {
        return "Erreur --> L'id du message doit être un entier";
    }
    if (this.bd.getMessage(idMessage) == null)
        return "Erreur --> Message inexistant";
    Message message = this.bd.getMessage(idMessage);
    if (!message.getLikes().contains(this.utilisateur)) {
        return "Erreur --> Vous n'avez pas liké ce message";
    }
    message.deleteLike(this.utilisateur);
    return "Vous ne likez plus le message " + idMessage;
}

/**
 * Méthode pour supprimer un message.
 *
 * @param commande Les éléments de la commande client.
 * @return Le résultat de l'opération.
 */
public String deleteMessage(String[] commande) {
    int idMessage;
    try {
        idMessage = Integer.parseInt(commande[1]);
    } catch (NumberFormatException e) {
        return "Erreur --> L'id du message doit être un entier";
    }
    Message message = this.bd.getMessage(idMessage);
    if (!this.utilisateur.getMessages().contains(message)) {
        return "Erreur --> Vous ne pouvez pas supprimer un message qui n'est pas le votre";
    }
    this.bd.deleteMessage(idMessage);
    this.utilisateur.deleteMessage(message);
    return "Vous avez supprimé le message " + idMessage;
}

/**
 * Méthode pour envoyer un message.
 *
 * @param message Le message à envoyer.
 */
private void envoieMessage(String message) {
    Message messageObjet = new Message(this.bd.getMessages().size(), message, this.utilisateur);
    this.bd.addMessage(messageObjet);
    this.utilisateur.addMessage(messageObjet);
    for (String follower : this.bd.getFollowers().getOrDefault(this.utilisateur.getPseudo(), new HashSet<>())) {
        PrintWriter followerWriter = Serveur.getClientHandler(follower).getWriter();
        followerWriter.println("(" + messageObjet.getId() + ") " + messageObjet.getAuteur().getPseudo() + " : " + messageObjet.getContenu());
    }
}
}