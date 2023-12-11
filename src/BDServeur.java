import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDServeur {

    private static Map<String, Utilisateur> utilisateurs;
    private static Map<Integer, Message> messages;
    private static Map<String, List<String>> followers;
    private static Map<String, List<String>> follows;

    private BDServeur() {}


    public static void load() {
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

    public static Map<String, List<String>> getFollowers() {
        return BDServeur.followers;
    }

    public static Map<String, List<String>> getFollows() {
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

    public static void addMessage(String contenu, Utilisateur auteur) {
        Message newMessage = new Message(contenu, auteur);
        BDServeur.messages.put(newMessage.getId(), newMessage);
    }

    public static void addFollower(String follower, String follow) {
        BDServeur.followers.get(follow).add(follower);
        BDServeur.follows.get(follower).add(follow);
    }

}