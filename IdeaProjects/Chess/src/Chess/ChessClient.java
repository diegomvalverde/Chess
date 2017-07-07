package Chess;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Developer: Diego Méndez Valverde.
 * Institution: Costa Rica Institute of Technology.
 * Mails: diego.mendez13@hotmail.es - diegomvalverde@outlook.com
 * GitHub: https://github.com/diegomvalverde
 * Creation date: 07/07/17.
 */
public class ChessClient
{
    public static String IP_SERVER = "localhost";       // Server Ip
    GameGUI ClientGUI;                                  // Cliente windows gui
    DataInputStream Input = null;                      // Read comunication
    DataOutputStream Output = null;                      // Write comunication
    Socket Client = null;                               // To conection
    String Username;                                    // Username

    /** Creates a new instance of Client */
    public ChessClient(GameGUI pGame) throws IOException
    {
        this.ClientGUI = pGame;
    }

    public void conection() throws IOException
    {
        try {
            Client = new Socket(ChessClient.IP_SERVER, 8081);

            // Start the in, out to comunication
            Input = new DataInputStream(Client.getInputStream());
            Output = new DataOutputStream(Client.getOutputStream());

            // The username is requested

            Username= JOptionPane.showInputDialog("Introducir Nick: ");

            // Set the name in the user gui

            ClientGUI.setTitle(Username);
            Output.writeUTF(Username);


        } catch (IOException e) {
            System.out.println("\tEl servidor no esta levantado");
            System.out.println("\t=============================");
        }

        // solo se le pasa entrada pues es solo para leer mensajes
        // el hiloCliente lee lo que el servidor le envia, opciones y como tiene referencia
        // a la ventana gato puede colocar en la pantalla cualquier cosa, como las
        //imagenes de X o O, llamar a metodo marcar, colocar el nombre de enemigo
        // o el suyo propio

        new ClientThread(Input, ClientGUI).start();
    }

    //GETTER AND SETTER
    public String getNombre()
    {
        return Username;
    }
}
