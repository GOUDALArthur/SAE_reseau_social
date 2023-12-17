import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BDServeur {

    private static Map<String, Utilisateur> utilisateurs;
    private static Map<Integer, Message> messages;
    private static Map<String, Set<String>> followers;
    private static Map<String, Set<String>> follows;

    private BDServeur() {}


    public static void load() {
        BDServeur.utilisateurs = new ConcurrentHashMap<>();
        BDServeur.messages = new ConcurrentHashMap<>();
        BDServeur.followers = new ConcurrentHashMap<>();
        BDServeur.follows = new ConcurrentHashMap<>();
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

    public static Utilisateur getUtilisateur(String pseudo) {
        System.out.println("getUtilisateur : " + pseudo);
        if (BDServeur.utilisateurs.containsKey(pseudo)) {
            System.out.println("getUtilisateur : " + pseudo + " existe");
            return BDServeur.utilisateurs.get(pseudo);
        }
        return null;
    }

    public static Message getMessage(int id) {
        if (BDServeur.messages.get(id) != null) {
            return BDServeur.messages.get(id);
        }
        return null;
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