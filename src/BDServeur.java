import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BDServeur {

    private Map<String, Utilisateur> utilisateurs;
    private Map<Integer, Message> messages;
    private Map<String, Set<String>> followers;
    private Map<String, Set<String>> follows;

    public BDServeur() {
        this.utilisateurs = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.followers = new ConcurrentHashMap<>();
        this.follows = new ConcurrentHashMap<>();
    }


    public void load() {
        //TODO : charger les utilisateurs et les messages depuis un fichier JSON
    }

    public void save() {
        //TODO : sauvegarder les utilisateurs et les messages dans un fichier JSON
    }


    public Map<String, Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public Map<Integer, Message> getMessages() {
        return this.messages;
    }

    public Map<String, Set<String>> getFollowers() {
        return this.followers;
    }

    public Map<String, Set<String>> getFollows() {
        return this.follows;
    }

    public Utilisateur getUtilisateur(String pseudo) {
        if (this.utilisateurs.containsKey(pseudo)) {
            return this.utilisateurs.get(pseudo);
        }
        return null;
    }

    public Message getMessage(int id) {
        if (this.messages.get(id) != null) {
            return this.messages.get(id);
        }
        return null;
    }


    public void addUtilisateur(String pseudo) {
        this.utilisateurs.put(pseudo, new Utilisateur(pseudo));
    }

    public void delUtilisateur(String pseudo) {
        this.utilisateurs.remove(pseudo);
    }

    public void addMessage(String contenu, Utilisateur auteur) {
        Message newMessage = new Message(this.messages.size(), contenu, auteur);
        this.messages.put(newMessage.getId(), newMessage);
    }

    public void delMessage(int id) {
        this.messages.remove(id);
    }

    public void addFollower(String followed, String follower) {
        // Ajout dans la liste des followers
        if (this.followers.containsKey(followed)) {
            this.followers.put(followed, new HashSet<>());
        }
        this.followers.get(followed).add(follower);

        // Ajout dans la liste des follows
        if (this.follows.containsKey(follower)) {
            this.follows.put(follower, new HashSet<>());
        }
        this.follows.get(follower).add(followed);
    }

    public void delFollower(String followed, String follower) {
        // Suppression dans la liste des followers
        if (this.followers.containsKey(followed)) {
            this.followers.get(followed).remove(follower);
        }

        // Suppression dans la liste des follows
        if (this.follows.containsKey(follower)) {
            this.follows.get(follower).remove(followed);
        }
    }

}