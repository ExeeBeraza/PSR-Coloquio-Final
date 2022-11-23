package pkgcoloquio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static pkgcoloquio.JuegoVida.BLOCK_SIZE;



public class Tablero extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {

    private Dimension d_gameBoardSize = null;
    private final ArrayList<Point> point = new ArrayList<>(0);

    private int i_movesPerSecond = 3;

    /* FUNCIONES PRINCIPALES */
    public Tablero(int i_movesPerSecond) {

        this.i_movesPerSecond = i_movesPerSecond;

        // Add resizing listener
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void actualizoTamanioArray() {
        ArrayList<Point> removeList = new ArrayList<>(0);
        for (Point current : point) {
            if ((current.x > d_gameBoardSize.width - 1) || (current.y > d_gameBoardSize.height - 1)) {
                removeList.add(current);
            }
        }
        point.removeAll(removeList);
        repaint();
    }

    public void addPunto(int x, int y) {
        if (!point.contains(new Point(x, y))) {
            point.add(new Point(x, y));
        }
        repaint();
    }

    /*  con eventos del mouse
    public void addPoint(MouseEvent me) {
        int x = me.getPoint().x/BLOCK_SIZE-1;
        int y = me.getPoint().y/BLOCK_SIZE-1;
        if ((x >= 0) && (x < d_gameBoardSize.width) && (y >= 0) && (y < d_gameBoardSize.height)) {
            addPoint(x,y);
        }
    }
     */
    public void eliminarPunto(int x, int y) {
        /*en tal posición elimino*/
        point.remove(new Point(x, y));
    }

    public void actualizarTablero() {
        //CHAUUUUU TABLERO
        point.clear();
    }

    public void llenarTaberoAzar(int percent) {
        for (int i = 0; i < d_gameBoardSize.width; i++) {
            for (int j = 0; j < d_gameBoardSize.height; j++) {
                if (Math.random() * 100 < percent) {
                    addPunto(i, j);
                }
            }
        }
    }

    @Override /*-->*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            for (Point newPoint : point) {
                //DIBUJAR UN NUEVO PUNTO
                g.setColor(Color.green);



                g.fillRect(BLOCK_SIZE + (BLOCK_SIZE * newPoint.x), BLOCK_SIZE + (BLOCK_SIZE * newPoint.y), BLOCK_SIZE, BLOCK_SIZE);
            }
        } catch (ConcurrentModificationException cme) {
        }
        /*CONFIGURACIÓN GRILLA*/
        g.setColor(Color.BLACK);
        for (int i = 0; i <= d_gameBoardSize.width; i++) {
            g.drawLine(((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE, (i * BLOCK_SIZE) + BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE * d_gameBoardSize.height));
        }
        for (int i = 0; i <= d_gameBoardSize.height; i++) {
            g.drawLine(BLOCK_SIZE, ((i * BLOCK_SIZE) + BLOCK_SIZE), BLOCK_SIZE * (d_gameBoardSize.width + 1), ((i * BLOCK_SIZE) + BLOCK_SIZE));
        }
    }

    @Override /*-->*/
    public void componentResized(ComponentEvent e) {
        /*Configurar el tamaño del tablero de juego con los límites adecuados*/
        d_gameBoardSize = new Dimension(getWidth() / BLOCK_SIZE - 2, getHeight() / BLOCK_SIZE - 2);
        actualizoTamanioArray();
    }

    @Override /*-->*/
    public void run() {
        // Defino el tablero con valores boleanos --> ¿Evoluciono o no?
        boolean[][] gameBoard = new boolean[d_gameBoardSize.width + 2][d_gameBoardSize.height + 2];
        // Recorro lo puntos de la grilla
        for (Point current : point) {
            //Es true en tal lugar
            gameBoard[current.x + 1][current.y + 1] = true;
        }
        /*nuevo vector donde se van a guardar todas las nuevas "evoluciones"*/
        ArrayList<Point> celulasobrevivientes = new ArrayList<>(0);
        /* Iterar a través de la matriz, seguir las reglas del juego de la vida */
        for (int i = 1; i < gameBoard.length - 1; i++) {
            for (int j = 1; j < gameBoard[0].length - 1; j++) {
                int surrounding = 0;   //Organismos

                //PREGUNTO EN TODAS LAS POSIBLES DIRECCIONES
                if (gameBoard[i - 1][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i - 1][j]) {
                    surrounding++;
                }
                if (gameBoard[i - 1][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j]) {
                    // Celula esta viva, Pudiendo ser 2 o 3
                    if ((surrounding == 2) || (surrounding == 3)) {
                        //agrego un nuevo punto al Array en tal posicion
                        celulasobrevivientes.add(new Point(i - 1, j - 1));
                    }
                } else {
                    // Celula esta muerta, si sucede que el valor es 3
                    if (surrounding == 3) {
                        celulasobrevivientes.add(new Point(i - 1, j - 1));
                    }
                }
            }
        }
        actualizarTablero();
        /*AGREGO EL NUEVO ARRAY AL PRINCIPAL (ponele)*/
        point.addAll(celulasobrevivientes);
        repaint();
        try {
            /* Duermo el hilo 1000 / en el contenido de movimiento por segundo --->3 */
            Thread.sleep(1000 / i_movesPerSecond);
            /* Dejo correr */
            run();
        } catch (InterruptedException ex) {
        }
    }

    /*MÉTODOS QUE SI NOS LO PONGO ME MARCA ERROR -->*/
    @Override
    public void componentMoved(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
