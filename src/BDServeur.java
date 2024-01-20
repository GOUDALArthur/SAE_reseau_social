import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BDServeur {

    /**
     * Instance unique de la base de données (singleton).
     */
    private static BDServeur instance = null;

    /**
     * Map des utilisateurs, associant un pseudo à un objet Utilisateur.
     */
    private Map<String, Utilisateur> utilisateurs;

    /**
     * Map des messages, associant un identifiant à un objet Message.
     */
    private Map<Integer, Message> messages;

    /**
     * Map des followers, associant un pseudo à un ensemble de pseudos qui le follow.
     */
    private Map<String, Set<String>> followers;

    /**
     * Map des follows, associant un pseudo à un ensemble de pseudos qu'il follow.
     */
    private Map<String, Set<String>> follows;

    /**
     * Constructeur privé du singleton.
     */
    private BDServeur() {}

    /**
     * Méthode permettant d'obtenir l'instance unique de la base de données.
     *
     * @return L'instance unique de la base de données.
     */
    public static BDServeur getInstance() {
        if (BDServeur.instance == null) {
            BDServeur.instance = new BDServeur();
        }
        return BDServeur.instance;
    }

    /**
     * Charge les données depuis les fichiers JSON.
     */
    public void load() {
        this.utilisateurs = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.followers = new ConcurrentHashMap<>();
        this.follows = new ConcurrentHashMap<>();
        JSONParser parser = new JSONParser();
        try (FileReader utilJson = new FileReader("bd/users.json");
             FileReader messJson = new FileReader("bd/messages.json")) {
            JSONArray data = (JSONArray) parser.parse(utilJson);
            for (Object obj : data) {
                JSONObject jsonObjet = (JSONObject) obj;
                String pseudo = (String) jsonObjet.get("nickname");
                this.utilisateurs.put(pseudo, new Utilisateur(pseudo));
                this.followers.put(pseudo, new HashSet<>());
                this.follows.put(pseudo, new HashSet<>());
            }
            for (Object obj : data) {
                JSONObject jsonObjet = (JSONObject) obj;
                String pseudo = (String) jsonObjet.get("nickname");
                JSONArray listFollowers = (JSONArray) jsonObjet.get("followers");
                Set<String> setFollowers = new HashSet<>();
                for (Object follower : listFollowers) {
                    String followerPseudo = (String) follower;
                    setFollowers.add(followerPseudo);
                    this.follows.get(followerPseudo).add(pseudo);
                }
                this.followers.put(pseudo, setFollowers);
            }
            
            data = (JSONArray) parser.parse(messJson);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            for (Object obj : data) {
                JSONObject jsonObjet = (JSONObject) obj;
                int id = ((Long) jsonObjet.get("id")).intValue();
                String pseudo = (String) jsonObjet.get("user");
                String contenu = (String) jsonObjet.get("content");
                String date = (String) jsonObjet.get("date");
                JSONArray listLikes = (JSONArray) jsonObjet.get("likes");
                Set<Utilisateur> setLikes = new HashSet<>();
                for (Object like : listLikes) {
                    setLikes.add(this.utilisateurs.get((String) like));
                }
                Utilisateur auteur = this.utilisateurs.get(pseudo);
                Date dateObjet = dateFormat.parse(date);
                Message message = new Message(id, contenu, auteur, dateObjet, setLikes);
                this.messages.put(id, message);
                auteur.addMessage(message);
            }
        } catch (IOException | ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        System.out.println(this.utilisateurs);
        System.out.println(this.messages);
        System.out.println(this.followers);
        System.out.println(this.follows);
    }

    /**
     * Sauvegarde les données dans les fichiers JSON.
     */
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

    /**
     * Donne la map des utilisateurs.
     *
     * @return La map des utilisateurs.
     */
    public Map<String, Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    /**
     * Donne la map des messages.
     *
     * @return La map des messages.
     */
    public Map<Integer, Message> getMessages() {
        return this.messages;
    }
    /**
     * Donne la map des followers.
     *
     * @return La map des followers.
     */
    public Map<String, Set<String>> getFollowers() {
        return this.followers;
    }
/**
     * Donne la map des follows.
     *
     * @return La map des follows.
     */
    public Map<String, Set<String>> getFollows() {
        return this.follows;
    }

    /**
     * Donne un utilisateur à partir de son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur recherché.
     * @return L'objet Utilisateur correspondant au pseudo, ou null si l'utilisateur n'existe pas.
     */
    public Utilisateur getUtilisateur(String pseudo) {
        return this.utilisateurs.getOrDefault(pseudo, null);
    }

    /**
     * Donne un message à partir de son identifiant.
     *
     * @param id L'identifiant du message recherché.
     * @return L'objet Message correspondant à l'identifiant, ou null si le message n'existe pas.
     */
    public Message getMessage(int id) {
        return this.messages.getOrDefault(id, null);
    }

    /**
     * Ajoute un utilisateur à la base de données.
     *
     * @param pseudo Le pseudo de l'utilisateur à ajouter.
     */
    public void addUtilisateur(String pseudo) {
        this.utilisateurs.put(pseudo, new Utilisateur(pseudo));
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param pseudo Le pseudo de l'utilisateur à supprimer.
     */
    public void removeUtilisateur(String pseudo) {
        this.utilisateurs.remove(pseudo);
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param utilisateur L'objet Utilisateur à supprimer.
     */
    public void removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur.getPseudo());
    }

    /**
     * Ajoute un message à la base de données.
     *
     * @param contenu Le contenu du message.
     * @param auteur L'auteur du message.
     */
    public void addMessage(String contenu, Utilisateur auteur) {
        Message newMessage = new Message(this.messages.size(), contenu, auteur);
        this.messages.put(newMessage.getId(), newMessage);
    }

    /**
     * Ajoute un message à la base de données.
     *
     * @param message L'objet Message à ajouter.
     */
    public void addMessage(Message message) {
        this.messages.put(message.getId(), message);
    }

    /**
     * Supprime un message de la base de données.
     *
     * @param id L'identifiant du message à supprimer.
     */
    public void deleteMessage(int id) {
        this.messages.remove(id);
    }

    /**
     * Supprime un message de la base de données.
     *
     * @param message L'objet Message à supprimer.
     */
    public void deleteMessage(Message message) {
        this.messages.remove(message.getId());
    }

    /**
     * Ajoute un follower à un utilisateur.
     *
     * @param followed Le pseudo de l'utilisateur qui follow.
     * @param follower Le pseudo de l'utilisateur qui se fait follow.
     */
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

    /**
     * Supprime un follower d'un utilisateur.
     *
     * @param followed Le pseudo de l'utilisateur qui follow.
     * @param follower Le pseudo de l'utilisateur qui se fait follow.
     */
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