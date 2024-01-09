import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientHandler implements Runnable {

    private Socket socketClient;
    private BufferedReader reader;
    private PrintWriter writer;
    private Utilisateur utilisateur;

    public ClientHandler(Socket socketClient, Utilisateur utilisateur) {
        this.socketClient = socketClient;
        this.utilisateur = utilisateur;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            this.writer = new PrintWriter(socketClient.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            writer.println("\n" + "Bienvenue, " + this.utilisateur.getPseudo() + " ! On est encore en test mais tkt ça arrive fort !" + "\n"); writer.flush();

            String message = this.reader.readLine();
            while (message != null) {
                System.out.println(message);
                message = this.reader.readLine();
            }
            scanner.close();
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
                this.utilisateur.deconnexion();
            }
        }
    }
}