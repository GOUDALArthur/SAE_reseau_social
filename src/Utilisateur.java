import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private String pseudo;
    private List<Message> messages;
    private int nbFollowers;
    private int nbFollowing;
    private boolean estConnecte;


    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
        this.estConnecte = false;
        this.messages = new ArrayList<>();
        this.nbFollowers = 0;
    }


    public String getPseudo() {
        return this.pseudo;
    }

    public boolean estConnecte() {
        return this.estConnecte;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public int getNbFollowers() {
        return this.nbFollowers;
    }

    public int getNbFollowing() {
        return this.nbFollowing;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void deleteMessage(Message message) {
        this.messages.remove(message);
    }

    public void connexion() {
        this.estConnecte = true;
    }

    public void deconnexion() {
        this.estConnecte = false;
    }


    //public void envoieMessage(String message) {
    //    BDServeur.addMessage(message, this);
    //}
}