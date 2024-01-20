import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message {

    private final int id;
    private final String contenu;
    private final Utilisateur auteur;
    private final Date date;
    private Set<Utilisateur> likes;
    private int nbLikes;


    public Message(int id, String contenu, Utilisateur auteur) {
        this.id = id;
        this.contenu = contenu;
        this.auteur = auteur;
        this.date = new Date();
        this.likes = new HashSet<>();
        this.nbLikes = 0;
    }

    public Message(int id, String contenu, Utilisateur auteur, Date date, Set<Utilisateur> likes) {
        this.id = id;
        this.contenu = contenu;
        this.date = date;
        this.auteur = auteur;
        this.likes = likes;
        this.nbLikes = likes.size();
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

    public Set<Utilisateur> getLikes() {
        return this.likes;
    }

    public int getNbLikes() {
        return this.nbLikes;
    }

    public void addLike(Utilisateur utilisateur) {
        this.likes.add(utilisateur);
        this.nbLikes++;
    }

    public void deleteLike(Utilisateur utilisateur) {
        this.likes.remove(utilisateur);
        this.nbLikes--;
    }

}