import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BDServeur {

    private static Map<String, Utilisateur> utilisateurs;
    private static Map<Integer, Message> messages;
    private static Map<String, Set<String>> followers;
    private static Map<String, Set<String>> follows;

    private BDServeur() {}


    public static void load() {
        BDServeur.utilisateurs = new HashMap<>();
        BDServeur.messages = new HashMap<>();
        BDServeur.followers = new HashMap<>();
        BDServeur.follows = new HashMap<>();
        //TODO : charger les utilisateurs et les messages depuis un fichier JSON
    }

    public static void save() {
        //TODO : sauvegarder les utilisateurs et les messages dans un fichier JSON
    }


    public static Map<String, Utilisateur> getUtilisateurs() {
        return BDServeur.utilisateurs;
    }

    public static Map<Integer, Message> getMessages() {
        return BDServeur.messages;
    }

    public static Map<String, Set<String>> getFollowers() {
        return BDServeur.followers;
    }

    public static Map<String, Set<String>> getFollows() {
        return BDServeur.follows;
    }

    public static Utilisateur getUtilisateur(String nomUtil) throws UtilisateurNotFoundException {
        if (BDServeur.utilisateurs.get(nomUtil) != null) {
            return BDServeur.utilisateurs.get(nomUtil);
        }
        throw new UtilisateurNotFoundException("L'utilisateur " + nomUtil + " n'existe pas");
    }

    public static Message getMessage(int id) throws MessageNotFoundException {
        if (BDServeur.messages.get(id) != null) {
            return BDServeur.messages.get(id);
        }
        throw new MessageNotFoundException("Le message " + id + " n'existe pas");
    }


    public static void addUtilisateur(String pseudo) {
        BDServeur.utilisateurs.put(pseudo, new Utilisateur(pseudo));
    }

    public static void delUtilisateur(String pseudo) {
        BDServeur.utilisateurs.remove(pseudo);
    }

    public static void addMessage(String contenu, Utilisateur auteur) {
        Message newMessage = new Message(contenu, auteur);
        BDServeur.messages.put(newMessage.getId(), newMessage);
    }

    public static void delMessage(int id) {
        BDServeur.messages.remove(id);
    }

    public static void addFollower(String followed, String follower) {
        // Ajout dans la liste des followers
        if (BDServeur.followers.containsKey(followed)) {
            BDServeur.followers.put(followed, new HashSet<>());
        }
        BDServeur.followers.get(followed).add(follower);

        // Ajout dans la liste des follows
        if (BDServeur.follows.containsKey(follower)) {
            BDServeur.follows.put(follower, new HashSet<>());
        }
        BDServeur.follows.get(follower).add(followed);
    }

    public static void delFollower(String followed, String follower) {
        // Suppression dans la liste des followers
        if (BDServeur.followers.containsKey(followed)) {
            BDServeur.followers.get(followed).remove(follower);
        }

        // Suppression dans la liste des follows
        if (BDServeur.follows.containsKey(follower)) {
            BDServeur.follows.get(follower).remove(followed);
        }
    }

}