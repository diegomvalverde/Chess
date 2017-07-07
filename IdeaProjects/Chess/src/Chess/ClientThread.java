package Chess;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Developer: Diego Méndez Valverde.
 * Institution: Costa Rica Institute of Technology.
 * Mails: diego.mendez13@hotmail.es - diegomvalverde@outlook.com
 * GitHub: https://github.com/diegomvalverde
 * Creation date: 07/07/17.
 */
public class ClientThread extends Thread
{
    // Only read
    DataInputStream Input;
    GameGUI ClientGUI;          // Game reference

    public ClientThread(DataInputStream pInput, GameGUI pClienteGUI) throws IOException
    {
        this.Input = pInput;
        this.ClientGUI = pClienteGUI;
    }
    public void run()
    {
        //VARIABLES
        String text ="",amigo="";
        int option = 0;

        while(true)
        {
            try{
                // esta leyendo siempre la instruccion, un int
                option = Input.readInt();

                switch(option)
                {
                    case 1:         // Message sended

                        int col = Input.readInt();        // Read Column
                        int row = Input.readInt();        // Read row
                        ClientGUI.mark(col,row);
                        break;


                    case 2:     // The username is readed

                        text = Input.readUTF();

                        // Set the enemy name

                        ClientGUI.setEnemy(text);
                        break;


                    case 3:     // Read the gamer num

                        ClientGUI.GamerNumber = Input.readInt();

                        ClientGUI.setEnemy(Input.readUTF());
                        break;


                    case 4:     // Read the message

                        text = Input.readUTF();

                        // Set the message

                        ClientGUI.showText(text);
                        break;


                    case 5:
                        // Read the col
                        int col1 = Input.readInt();
                        // Read the row
                        int fil1 = Input.readInt();
                        // ...
                        ClientGUI.bomba(col1,fil1);

                        System.out.println("Op5: recibe columna fila para bomba ");
                        break;

                }
            }

            catch (IOException e)
            {
                System.out.println("Error en la comunicación "+"Informaciṕn para el usuario");
                break;
            }
        }

        System.out.println("Se desconectó el servidor");
    }

}
