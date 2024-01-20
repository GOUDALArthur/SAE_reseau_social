import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    /**
     * Le pseudo de l'utilisateur.
     */
    private String pseudo;

    /**
     * Liste des messages de l'utilisateur.
     */
    private List<Message> messages;

    /**
     * Nombre de followers de l'utilisateur.
     */
    private int nbFollowers;

    /**
     * Nombre d'utilisateurs que l'utilisateur follow.
     */
    private int nbFollowing;

    /**
     * Le statut de connexion de l'utilisateur.
     */
    private boolean estConnecte;

    /**
     * Le constructeur de la classe.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     */
    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
        this.messages = new ArrayList<>();
        this.nbFollowers = 0;
        this.nbFollowing = 0;
        this.estConnecte = false;
    }

    /**
     * Donne le pseudo de l'utilisateur.
     *
     * @return Le pseudo de l'utilisateur.
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Vérifie si l'utilisateur est actuellement connecté.
     *
     * @return True si l'utilisateur est connecté, false sinon.
     */
    public boolean estConnecte() {
        return this.estConnecte;
    }

    /**
     * Donne la liste des messages envoyées par l'utilisateur.
     *
     * @return La liste des messages.
     */
    public List<Message> getMessages() {
        return this.messages;
    }

    /**
     * Donne le nombre de followers de l'utilisateur.
     *
     * @return Le nombre de followers.
     */
    public int getNbFollowers() {
        return this.nbFollowers;
    }

    /**
     * Donne le nombre d'utilisateurs que l'utilisateur follow.
     *
     * @return Le nombre d'utilisateurs follow.
     */
    public int getNbFollowing() {
        return this.nbFollowing;
    }

    /**
     * Change le pseudo de l'utilisateur.
     *
     * @param pseudo Le nouveau pseudo de l'utilisateur.
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Ajoute un message à la liste des messages envoyées par l'utilisateur.
     *
     * @param message Le message à ajouter.
     */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * Supprime un message de la liste des messages envoyées par l'utilisateur.
     *
     * @param message Le message à supprimer.
     */
    public void deleteMessage(Message message) {
        this.messages.remove(message);
    }

    /**
     * Connecte l'utilisateur.
     */
    public void connexion() {
        this.estConnecte = true;
    }

    /**
     * Déconnecte l'utilisateur.
     */
    public void deconnexion() {
        this.estConnecte = false;
    }

}
