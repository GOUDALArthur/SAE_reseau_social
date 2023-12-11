import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private String pseudo;
    private boolean estConnecte;
    private List<Message> messages;
    private int nbFollowers;
    private int nbFollowing;
    private String ip;


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

    public String getIp() {
        return this.ip;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void connexion(String ip) {
        this.estConnecte = true;
        this.ip = ip;
    }

    public void deconnexion() {
        this.estConnecte = false;
    }


    public void follow(String nomUtil) {
        try {
            Utilisateur newFollow = BDServeur.getUtilisateur(nomUtil);
            newFollow.addFollower(this);
            
        }
        catch (UtilisateurNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unfollow(String nomUtil) {
        try {
            Utilisateur newUnfollow = BDServeur.getUtilisateur(nomUtil);
            newUnfollow.delFollower(this);
            this.following.remove(newUnfollow);
        }
        catch (UtilisateurNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void like(int idMessage) {
        try {
            BDServeur.getMessage(idMessage).addLike(this);
        }
        catch (MessageNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dislike(int idMessage) {
        try {
            BDServeur.getMessage(idMessage).delLike(this);
        }
        catch (MessageNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteMessage(int idMessage) {
        try {
            Message message = BDServeur.getMessage(idMessage);
            this.messages.remove(message);
            BDServeur.getMessages().remove(idMessage);
        }
        catch (MessageNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    public void addFollower(Utilisateur utilisateur) {
        this.followers.add(utilisateur);
        this.nbFollowers++;
    }

    public void delFollower(Utilisateur utilisateur) {
        this.followers.remove(utilisateur);
        this.nbFollowers--;
    }

}