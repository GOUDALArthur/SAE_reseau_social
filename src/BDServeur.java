import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class BDServeur {

    private static BDServeur instance = null;
    private Map<String, Utilisateur> utilisateurs;
    private Map<Integer, Message> messages;
    private Map<String, Set<String>> followers;
    private Map<String, Set<String>> follows;

    private BDServeur() {}

    public static BDServeur getInstance() {
        if (BDServeur.instance == null) {
            BDServeur.instance = new BDServeur();
        }
        return BDServeur.instance;
    }

    public void load() {
        this.utilisateurs = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.followers = new ConcurrentHashMap<>();
        this.follows = new ConcurrentHashMap<>();
        //TODO : charger les utilisateurs et les messages depuis un fichier JSON
    }

    public void save() {
        //TODO : sauvegarder les utilisateurs et les messages dans un fichier JSON
        try (FileWriter json = new FileWriter("utilisateurs.json")) {
            JSONObject util = new JSONObject();
            for (Map.Entry<String, Utilisateur> entry : this.utilisateurs.entrySet()) {
                String pseudo = entry.getKey();
                util = new JSONObject();
                util.put("pseudo",pseudo);
                Set<String> setFollowers = this.followers.getOrDefault(pseudo, new HashSet<>());
                JSONArray listFollowers = new JSONArray();
                listFollowers.addAll(setFollowers);
                util.put("followers", listFollowers);
            }
            json.write(util.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return this.utilisateurs.getOrDefault(pseudo, null);
    }

    public Message getMessage(int id) {
        return this.messages.getOrDefault(id, null);
    }


    public void addUtilisateur(String pseudo) {
        this.utilisateurs.put(pseudo, new Utilisateur(pseudo));
    }

    public void removeUtilisateur(String pseudo) {
        this.utilisateurs.remove(pseudo);
    }

    public void removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur.getPseudo());
    }

    public void addMessage(String contenu, Utilisateur auteur) {
        Message newMessage = new Message(this.messages.size(), contenu, auteur);
        this.messages.put(newMessage.getId(), newMessage);
    }

    public void deleteMessage(int id) {
        this.messages.remove(id);
    }

    public void deleteMessage(Message message) {
        this.messages.remove(message.getId());
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