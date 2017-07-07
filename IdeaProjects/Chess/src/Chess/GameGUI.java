package Chess;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.io.IOException;

/**
 * Developer: Diego Méndez Valverde.
 * Institution: Costa Rica Institute of Technology.
 * Mails: diego.mendez13@hotmail.es - diegomvalverde@outlook.com
 * GitHub: https://github.com/diegomvalverde
 * Creation date: 07/07/17.
 */
public class GameGUI extends  JFrame{
    private JTextArea ReadMessagesArea;
    private JTextField WriteMessagesArea;
    private JButton SendButton;
    private JLabel UsersLabel;
    private JPanel MessagesPane;
    private JPanel ChessAreaPane;
    private JPanel UsersPane;


    public GameGUI() {
        try {
            generarTablero();

            cliente = new ChessClient(this);
            cliente.conection();
            cliente.Output.writeInt(3);

        }
        catch (IOException ignored)
        {

        }
    }


    //----------------------------------
    private ChessClient cliente;
    //----------------------------------


    // ChessDimensions

    private int DIMENSIONES = IConstants.CHESSDIMENSIONS;

    private JButton[][] LabelsBoard = new JButton[DIMENSIONES][DIMENSIONES];

    private int[][] LogicBoard = new int[DIMENSIONES][DIMENSIONES];

    // Images

    private ImageIcon iconoVacio = new ImageIcon(getClass().getResource("cvacio.GIF"));

    private int Turn = 1;

    //numero de jugador 1 o 2
    private int GamerNumber = 0;


    private void buildBoard()
    {
        for(int i = 0; i < DIMENSIONES; i++)
        {
            for(int j = 0; j < DIMENSIONES; j++)
            {
                // Set a black image

                LabelsBoard[i][j] = new JButton(iconoVacio);

                // Set the buttom to the panel

                this.ChessAreaPane.add(LabelsBoard[i][j]);

                // Set position and laction

                LabelsBoard[i][j].setBounds(100+50*i, 100+50*j, 50, 50);

                // Set comand
                LabelsBoard[i][j].setActionCommand(i+","+j);//i+","+j

                // Action
                LabelsBoard[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        clickSobreTablero(evt);
                    }
                });

                LogicBoard[i][j] = 0; // Enable
            }
        }
    }

    // restart the game
    public void reiniciarJuego()
    {
        Turn = 1;
        for(int i = 0; i < DIMENSIONES; i++)
        {
            for(int j=0;j<DIMENSIONES;j++)
            {
                tableroLabels[i][j].setIcon(iconoVacio);
                tableroLogico[i][j]=0;
            }
        }
    }

    // este metodo es la respuesta del cliente al clic del enemigo
    public void marcar(int columna, int fila)
    {
        // marca el tablero con num de jugador
        tableroLogico[columna][fila]=turnoJugador;
        // si soy el 1, marco con o que es el 2, sino con X
        // pues es el turno del enemigo que estoy marcando
        if (numeroJugador == 1)
            tableroLabels[columna][fila].setIcon(iconoCirculo);
        else
            tableroLabels[columna][fila].setIcon(iconoEquiz);

        // pregunta si gano el enemigo
        if(haGanado())
        {
            JOptionPane.showMessageDialog(null, "Ha ganado el jugador "+turnoJugador);

            reiniciarJuego();
        }
        // este fue el clic del enemigo, marco ahora mi turno
        turnoJugador = numeroJugador;
        jLabel1.setText("Turno del Jugador "+turnoJugador);


    }

    public void bomba(int col, int fila)
    {
        JOptionPane.showMessageDialog(this, "Generar bombas y enviarlas una " +
                "a una al enemigo ("+col+","+fila+")");
    }

    public void clickSobreTablero(java.awt.event.MouseEvent evt)
    {
        // obtiene el boton
        JButton botonTemp = (JButton)evt.getComponent();
        // obtiene el i,j de action command del boton
        String identificadorBoton = botonTemp.getActionCommand();

        // separa el string del action comand para obtener columnas
        int columna =
                Integer.parseInt(identificadorBoton.substring(0,identificadorBoton.indexOf(",")));
        int fila =
                Integer.parseInt(identificadorBoton.substring(1+identificadorBoton.indexOf(",")));

        // si ya se disparo entonces nada
        if(tableroLogico[columna][fila]!=0)
            return;

        // si es mi turno continua, si no return
        if (numeroJugador != turnoJugador)
            return;

        // como es turno del cliente marca el logico con su numero
        tableroLogico[columna][fila]=turnoJugador;
        // si era el jugador 1 marca con x y cambia el turno a jugador 2
        if (numeroJugador == 1)
        {

            tableroLabels[columna][fila].setIcon(iconoEquiz);
            turnoJugador=2;
        }
        else
        {
            // si era jugador 3, marca circulo y turno jugador 1
            tableroLabels[columna][fila].setIcon(iconoCirculo);
            turnoJugador=1;
        }
        // muestra el turno del jugador
        jLabel1.setText("Turno del Jugador "+turnoJugador);

        try {
            // como el cliente dio clic debe enviar al servidor las coordenadas
            // el servidor se las pasara al thread cliente para que este
            // las muestre (haga el marcar)
            // envia las coordenadas
            cliente.salida.writeInt(1);
            cliente.salida.writeInt(columna);
            cliente.salida.writeInt(fila);
        } catch (IOException ex) {

        }

        // si gano el jugador 1 lo indica
        if(haGanado())
        {
            JOptionPane.showMessageDialog(null, "Ha ganado el jugador 1");
            reiniciarJuego();
        }
    }


    boolean haGanado()
    {

        //Ganó en las filas
        for(int i=0;i<3;i++)
        {
            if ((tableroLogico[i][0]==tableroLogico[i][1])
                    &&(tableroLogico[i][1]==tableroLogico[i][2])
                    && !(tableroLogico[i][0]==0))
            {
                return true;
            }
        }

        //Gano en las columnas
        for(int i=0;i<3;i++)
        {
            if ((tableroLogico[0][i]==tableroLogico[1][i])
                    &&(tableroLogico[1][i]==tableroLogico[2][i])
                    && !(tableroLogico[0][i]==0))
            {
                return true;
            }
        }
        //Verificar diagonal 1
        if ((tableroLogico[0][0]==tableroLogico[1][1])
                &&(tableroLogico[1][1]==tableroLogico[2][2])
                && !(tableroLogico[0][0]==0))
            return true;

        //Verificar diagonal 2
        if ((tableroLogico[2][0]==tableroLogico[1][1])
                &&(tableroLogico[1][1]==tableroLogico[0][2])
                && !(tableroLogico[2][0]==0))
            return true;

        return false;
    }

    public void setEnemigo(String enem)
    {
        this.UsersLabel.setText("vs. "+enem);
    }

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        try {

            // Get the text

            String mensaje = this.ReadMessagesArea.getText();


            // Add to the text area

            this.ReadMessagesArea.append(cliente.Username+"> "+ mensaje + "\n");


            // Clean the textfield
            this.ReadMessagesArea.setText("");

            // envia al server la opcion 4 para que le pase al enemigo
            // lo escrito
            cliente.salida.writeInt(4);
            // le envia el mensaje
            cliente.salida.writeUTF(cliente.nomCliente+"> "+mensaje);

        }
        catch (IOException ex)
        {

        }


    }

    void showText(String texto)
    {
        this.ReadMessagesArea.append(texto+"\n");
    }

    // End of variables declaration

//    void setTitle()
//    {
//        this.setTitle();
//    }
}
