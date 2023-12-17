import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur implements Runnable {

    private String pseudo;
    private List<Message> messages;
    private int nbFollowers;
    private int nbFollowing;

    private Socket socketClient;
    private boolean estConnecte;
    private BufferedReader reader;
    private PrintWriter writer;


    public Utilisateur(String pseudo) {
        this.pseudo = pseudo;
        this.estConnecte = false;
        this.messages = new ArrayList<>();
        this.nbFollowers = 0;
    }

    public void setSocket(Socket socketClient) {
        this.socketClient = socketClient;
        try {
            reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            writer = new PrintWriter(socketClient.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (this.writer != null) {
            BDServeur.addMessage(message, this);
            this.writer.println(message);
        }
    }


    @Override
    public void run() {
        try (
            BufferedReader tempoReader = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
            PrintWriter tempoWriter = new PrintWriter(this.socketClient.getOutputStream());
        )  {
            this.reader = tempoReader;
            this.writer = tempoWriter;

            writer.println("Bienvenue, " + this.pseudo + " ! On est encore en test mais tkt ça arrive fort !"); writer.flush();

            System.out.print("Envoyer un message : ");
            String message = this.reader.readLine();
            while (message != null) {
                this.envoieMessage(message);
                System.out.print("Envoyer un message : ");
                message = this.reader.readLine();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                this.reader.close();
                this.writer.close();
                this.socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                this.deconnexion();
            }
        }
    }

}