package pkgcoloquio;
//-----------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;/*Excepción lanzada por métodos que han detectado modificaciones simultáneas de un objeto cuando se modifica no está permitida.*/
import javax.swing.*;

public class JuegoVida extends JFrame implements ActionListener {

    /* Métodos que setean por default la ventana */
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
    public static final int BLOCK_SIZE = 10;

    /* Métodos abstractos */
    /* INICIALIZACIÓN DE VARIABLES */
    private final JMenuBar mb_menu;
    private final JMenu m_file;
    private final JMenu m_game;
    private final JMenuItem mi_file_options;
    private final JMenuItem mi_game_autofill;
    private final JMenuItem mi_game_play;
    private final JMenuItem mi_game_stop;
    private final JMenuItem mi_game_reset;

    public int i_movesPerSecond = 3;
    private final Tablero gb_gameBoard;
    private Thread game;

    /* PRINCIPAL */
    public static void main(String[] args) {
        /*Seteo especificaciones de la ventana*/
        JFrame game = new JuegoVida();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setTitle("Autómatas Celulares --> Game of LIFE");
        game.setBackground(Color.BLACK);
        game.setSize(DEFAULT_WINDOW_SIZE);
        game.setMinimumSize(MINIMUM_WINDOW_SIZE);
        game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight()) / 2);
        game.setVisible(true);
    }

    /* METODOS ABSTRACTOS */
    public JuegoVida() {
        //MENÚ
        mb_menu = new JMenuBar();
        setJMenuBar(mb_menu);
        m_file = new JMenu("Indice");
        mb_menu.add(m_file);
        m_game = new JMenu("Juego");
        mb_menu.add(m_game);

        mi_file_options = new JMenuItem("Opciones");
        mi_file_options.addActionListener(this);

        m_file.add(mi_file_options);
        m_file.add(new JSeparator());
        /*MÁS DEL MENÚ*/
        mi_game_autofill = new JMenuItem("SubIndice");
        mi_game_autofill.addActionListener(this);
        mi_game_play = new JMenuItem("JUEGO!");
        mi_game_play.addActionListener(this);
        mi_game_stop = new JMenuItem("PARA");
        mi_game_stop.setEnabled(false);
        mi_game_stop.addActionListener(this);
        mi_game_reset = new JMenuItem("RESETEO");
        mi_game_reset.addActionListener(this);
        m_game.add(mi_game_autofill);
        m_game.add(new JSeparator());
        m_game.add(mi_game_play);
        m_game.add(mi_game_stop);
        m_game.add(mi_game_reset);

        //SETEO TABLERO JUEGO----
        gb_gameBoard = new Tablero(i_movesPerSecond);
        add(gb_gameBoard);
    }

    /* SETEO EL COMIENZO DEL JUEGO PARA EL JUGADOR --> en caso de que haya */
    public void inicioJuego(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            //jugar inhabilitado
            mi_game_play.setEnabled(false);
            //jugar desahbilitado
            mi_game_stop.setEnabled(true);
            game = new Thread(gb_gameBoard);    //CREO EL HILO PARA QUE JUEGE
            game.start();                   //INICIO EL HILO
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);
            //juego interrumpido
            game.interrupt();
        }
    }

    @Override  /*-->*/
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource().equals(mi_file_options)) {
            // Put up an options panel to change the number of moves per second
            final JFrame f_options = new JFrame();
            f_options.setTitle("Opciones");
            f_options.setSize(300, 270);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth()) / 2,
                    (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight()) / 2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);
            p_options.add(new JLabel("Número de movimientos por segundo:"));  /*TIEMPO DADO --> ITERACCIONES*/
            Integer[] secondOptions = {1, 2, 3, 4, 5, 10, 15, 20};
            final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(i_movesPerSecond);
            cb_seconds.addActionListener((ActionEvent ae1) -> {
                i_movesPerSecond = (Integer) cb_seconds.getSelectedItem();
                f_options.dispose();
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_autofill)) {
            
            final JFrame f_autoFill = new JFrame();
            f_autoFill.setTitle("Selección");
            f_autoFill.setSize(360, 340);
            f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth()) / 2,
                    (Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight()) / 2);
            f_autoFill.setResizable(false);
            JPanel p_autoFill = new JPanel();
            p_autoFill.setOpaque(false);
            f_autoFill.add(p_autoFill);
            
            p_autoFill.add(new JLabel("Porcentaje a llenarse: "));
            Object[] percentageOptions = {"Select", 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 95};
            final JComboBox cb_percent = new JComboBox(percentageOptions);
            
            p_autoFill.add(cb_percent);
            cb_percent.addActionListener((ActionEvent e) -> {
                if (cb_percent.getSelectedIndex() > 0) {
                    gb_gameBoard.actualizarTablero();
                    gb_gameBoard.llenarTaberoAzar((Integer) cb_percent.getSelectedItem());
                    f_autoFill.dispose();
                }
            });
            f_autoFill.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            gb_gameBoard.actualizarTablero();
            gb_gameBoard.repaint();
        } else if (ae.getSource().equals(mi_game_play)) {
            inicioJuego(true);
        } else if (ae.getSource().equals(mi_game_stop)) {
            inicioJuego(false);
        }
    }

}
