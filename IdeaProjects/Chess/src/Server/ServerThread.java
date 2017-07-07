package Server;

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
public class ServerThread
{
    private Socket Client = null;               // Reference to clients socket
    private DataInputStream Input = null;       // To read the comunication
    private DataOutputStream Output = null;     // To send the comunication
    private String UserName;                    // For the conection use name
    private ChessServer Server;                 // Server reference
    private ServerThread Enemy = null;          // To send messages
    private int ClientNumber;                   // Player number

    ServerThread (Socket pCliente, ChessServer pServer, int pClientNumber)
    {
        this.Client = pCliente;
        this.Server = pServer;
        this.ClientNumber = pClientNumber;
        this.UserName = "";                     // At this point the name is not readed.
    }

    void setEnemy(ServerThread enemy) {
        Enemy = enemy;
    }

    private String getNameUser()
    {
        return UserName;
    }
    private void setNameUser(String name)
    {
        this.UserName = name;
    }

    void run()
    {
        try
        {
            // Comunication

            Input = new DataInputStream(Client.getInputStream());
            Output = new DataOutputStream(Client.getOutputStream());

            // This is the first read, the username

            System.out.println("lee el nombre");
            this.setNameUser(Input.readUTF());
            System.out.println("1. Leyo nombre: " + UserName);
            enviaUser();    // Send the name to tghe other user (ClientNumber = 2)

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Variables

        int Option;

        while(true)
        {
            try
            {
                // It allways waits for the int, the instruction
                Option = Input.readInt();
                switch(Option)
                {
                    // Send the coordinate
                    case 1:
                        // Read the coordinates that client send
                        // Show the enemy the coordinates to mark the chess

                        int Column = Input.readInt();               // Read the coordinate
                        int Row = Input.readInt();                  // Read the row
                        Server.MyTerminal.showMessage("Recibido " + Column +","+Row);

                        // Send the info to the enemy

                        Enemy.Output.writeInt(1);            // Option 1 to the enemy server
                        Enemy.Output.writeInt(Column);         // Send Column
                        Enemy.Output.writeInt(Row);            // Send Row

                        System.out.println("Op1: lee col,fil, envia al enemigo, 1, col, fila: "+Column+" , "+Row);

                        break;

                    case 2:

                        break;

                    case 3: //le envia el status, que es el numero de jugador y el nombre enemigo
                        Output.writeInt(3);
                        Output.writeInt(ClientNumber);

                        if (Enemy != null)
                            Output.writeUTF(Enemy.UserName);
                        else
                            Output.writeUTF("");
                        break;


                    case 4:
                        // Read Message

                        String mensaje = Input.readUTF();

                        // Send 4 to enemy ServerThread

                        Enemy.Output.writeInt(4);

                        // Send  Message

                        Enemy.Output.writeUTF(mensaje+"OtraCosa");

                        System.out.println("Op4: envia 4 y mensaje: "+ mensaje);

                        break;

                }
            }

            catch (IOException e)
            {
                System.out.println("El cliente termino la conexion");
                break;
            }

        }
        Server.MyTerminal.showMessage("Se removio un usuario");

        try
        {
            Server.MyTerminal.showMessage("Se desconecto un usuario: "+ UserName);
            Client.close();
        }

        catch(Exception et)
        {
            Server.MyTerminal.showMessage("No se puede cerrar el socket");
        }
    }

    // Send names
    private void enviaUser()
    {
        if (Enemy != null)
        {
            try
            {
            Enemy.Output.writeInt(2);                               // Send option 2
                Enemy.Output.writeUTF(this.getNameUser());              // Write Username
                System.out.println("2. Envia 2 y username "+ "2" +getNameUser());
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
