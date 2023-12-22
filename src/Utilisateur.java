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

    public void connexion() {
        this.estConnecte = true;
    }

    public void deconnexion() {
        this.estConnecte = false;
    }


    public void follow(String newFollow) {
        BDServeur.addFollower(newFollow, this.pseudo);
    }

    public void unfollow(String nomFollow) {
        BDServeur.delFollower(nomFollow, this.pseudo);
    }

    public void like(int idMessage) {
        Message message = BDServeur.getMessage(idMessage);
        if (message != null) {
            message.addLike(this);
        }
        else { System.out.println("Message inexistant : " + idMessage); }
    }

    public void dislike(int idMessage) {
        Message message = BDServeur.getMessage(idMessage);
        if (message != null) {
            message.delLike(this);
        }
        else { System.out.println("Message inexistant : " + idMessage); }
    }

    public void deleteMessage(int idMessage) {
        Message message = BDServeur.getMessage(idMessage);
        if (message != null) {
            if (message.getAuteur() != this) {
                System.out.println("Vous n'êtes pas l'auteur de ce message.");
            }
            else {
                this.messages.remove(message);
                BDServeur.getMessages().remove(idMessage);
            }
        }
        else { System.out.println("Message inexistant : " + idMessage); }
    }

    public void envoieMessage(String message) {
        BDServeur.addMessage(message, this);
    }
}