import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket socketServer = new ServerSocket(Serveur.PORT)) {
            Socket socketClient = socketServer.accept();
            System.out.println("Connexion d'un client");
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
