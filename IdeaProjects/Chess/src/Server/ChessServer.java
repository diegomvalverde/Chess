package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Developer: Diego Méndez Valverde.
 * Institution: Costa Rica Institute of Technology.
 * Mails: diego.mendez13@hotmail.es - diegomvalverde@outlook.com
 * GitHub: https://github.com/diegomvalverde
 * Creation date: 07/07/17.
 */

public class ChessServer {

    ServerTerminal MyTerminal;

    public ChessServer()
    {
        this.MyTerminal = new ServerTerminal();
    }

    public void runServer()
    {
        Socket Client1;
        Socket Client2;
        try {
            // This build the server to receive the conections
            ServerSocket serv = new ServerSocket(8081);
            MyTerminal.showMessage(".:: Servidor Activo");
            MyTerminal.showMessage(".:: Esperando dos usuarios");

            // Whait for first client
            Client1 = serv.accept();
            MyTerminal.showMessage(".:: Primer Cliente Aceptado");
            ServerThread User1 = new ServerThread(Client1, this,1);
            User1.run();

            // espera segundo cliente
            Client2 = serv.accept();
            MyTerminal.showMessage(".:: Segundo Cliente Aceptado");
            ServerThread User2 = new ServerThread(Client2, this,2);
            User2.run();

            // Add enemy
            User1.setEnemy(User2);
            User2.setEnemy(User1);


        } catch (IOException ex)
        {
            MyTerminal.showMessage("ERROR ... en el servidor");
        }


    }
}
