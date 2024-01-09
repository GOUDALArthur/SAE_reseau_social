import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {

    private final int id;
    private final String contenu;
    private final Date date;
    private final Utilisateur auteur;
    private List<Utilisateur> likes;
    private int nbLikes;


    public Message(int id, String contenu, Utilisateur auteur) {
        this.id = id;
        this.contenu = contenu;
        this.auteur = auteur;
        this.date = new Date();
        this.likes = new ArrayList<>();
    }


    public int getId() {
        return this.id;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Date getDate() {
        return this.date;
    }

    public Utilisateur getAuteur() {
        return this.auteur;
    }

    public List<Utilisateur> getLikes() {
        return this.likes;
    }

    public int getNbLikes() {
        return this.nbLikes;
    }

    public void addLike(Utilisateur utilisateur) {
        this.likes.add(utilisateur);
        this.nbLikes++;
    }

    public void delLike(Utilisateur utilisateur) {
        this.likes.remove(utilisateur);
        this.nbLikes--;
    }

}