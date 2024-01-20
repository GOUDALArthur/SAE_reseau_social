import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message {

    /**
     * Identifiant du message.
     */
    private final int id;

    /**
     * Texte du message.
     */
    private final String contenu;

    /**
     * Auteur du message.
     */
    private final Utilisateur auteur;

    /**
     * Date du message.
     */
    private final Date date;

    /**
     * Ensemble d'utilisateurs qui ont aimé le message.
     */
    private Set<Utilisateur> likes;

    /**
     * Nombre de likes du message.
     */
    private int nbLikes;

    /**
     * Constructeur d'un nouveau message.
     *
     * @param id      Identifiant du message.
     * @param contenu Texte du message.
     * @param auteur  Auteur du message.
     */
    public Message(int id, String contenu, Utilisateur auteur) {
        this.id = id;
        this.contenu = contenu;
        this.auteur = auteur;
        this.date = new Date();
        this.likes = new HashSet<>();
        this.nbLikes = 0;
    }

    /**
     * Constructeur plus précis.
     *
     * @param id      Identifiant du message.
     * @param contenu Texte du message.
     * @param auteur  Auteur du message.
     * @param date    Date du message.
     * @param likes   Ensemble d'utilisateurs qui ont aimé le message.
     */
    public Message(int id, String contenu, Utilisateur auteur, Date date, Set<Utilisateur> likes) {
        this.id = id;
        this.contenu = contenu;
        this.date = date;
        this.auteur = auteur;
        this.likes = likes;
        this.nbLikes = likes.size();
    }

    /**
     * Donne l'identifiant du message.
     *
     * @return L'identifiant du message.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Donne le contenu texte du message.
     *
     * @return Le contenu texte du message.
     */
    public String getContenu() {
        return this.contenu;
    }

    /**
     * Donne la date du message .
     *
     * @return La date du message.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Donne l'auteur du message.
     *
     * @return L'auteur du message.
     */
    public Utilisateur getAuteur() {
        return this.auteur;
    }

    /**
     * Donne l'ensemble d'utilisateurs qui ont aimé le message.
     *
     * @return L'ensemble d'utilisateurs qui ont aimé le message.
     */
    public Set<Utilisateur> getLikes() {
        return this.likes;
    }

    /**
     * Donne le nombre total de likes du message.
     *
     * @return Le nombre total de likes du message.
     */
    public int getNbLikes() {
        return this.nbLikes;
    }

    /**
     * Ajoute un like au message.
     *
     * @param utilisateur L'utilisateur qui a aimé le message.
     */
    public void addLike(Utilisateur utilisateur) {
        this.likes.add(utilisateur);
        this.nbLikes++;
    }

    /**
     * Supprime le like d'un utilisateur.
     *
     * @param utilisateur L'utilisateur dont le like doit être supprimé.
     */
    public void deleteLike(Utilisateur utilisateur) {
        this.likes.remove(utilisateur);
        this.nbLikes--;
    }
}
