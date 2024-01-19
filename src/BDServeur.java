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
        try (FileWriter utilJson = new FileWriter("bd/users.json");
             FileWriter messJson = new FileWriter("bd/messages.json")) {
            JSONArray data = new JSONArray();
            JSONObject obj = new JSONObject();
            for (Map.Entry<String, Utilisateur> entry : this.utilisateurs.entrySet()) {
                String pseudo = entry.getKey();
                obj = new JSONObject();
                obj.put("nickname",pseudo);
                Set<String> setFollowers = this.followers.getOrDefault(pseudo, new HashSet<>());
                JSONArray listFollowers = new JSONArray();
                listFollowers.addAll(setFollowers);
                obj.put("followers", listFollowers);
                data.add(obj);
            }
            utilJson.write(data.toJSONString());
            data = new JSONArray();
            for (Map.Entry<Integer, Message> entry : this.messages.entrySet()) {
                int id = entry.getKey();
                Message message = entry.getValue();
                obj = new JSONObject();
                obj.put("id", id);
                obj.put("user", message.getAuteur().getPseudo());
                obj.put("content", message.getContenu());
                obj.put("date", message.getDate().toString());
                Set<String> setLikes = new HashSet<>();
                for (Utilisateur utilisateur : message.getLikes()) {
                    setLikes.add(utilisateur.getPseudo());
                }
                JSONArray listLikes = new JSONArray();
                listLikes.addAll(setLikes);
                obj.put("likes", listLikes);
                data.add(obj);
            }
            messJson.write(data.toJSONString());
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

    public void addMessage(Message message) {
        this.messages.put(message.getId(), message);
    }

    public void deleteMessage(int id) {
        this.messages.remove(id);
    }

    public void deleteMessage(Message message) {
        this.messages.remove(message.getId());
    }

    public void addFollower(String followed, String follower) {
        // Ajout dans la liste des followers
        if (!this.followers.containsKey(followed)) {
            this.followers.put(followed, new HashSet<>());
        }
        this.followers.get(followed).add(follower);

        // Ajout dans la liste des follows
        if (!this.follows.containsKey(follower)) {
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