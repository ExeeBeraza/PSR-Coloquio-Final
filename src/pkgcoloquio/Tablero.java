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

    public void eliminarPunto(int x, int y) {
        point.remove(new Point(x, y));
    }

    public void limpiarTablero() {
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
        long tiempoInicio = System.currentTimeMillis();

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
        Acomodador acomodador = new Acomodador(gameBoard,celulasobrevivientes);
        acomodador.start();

        try{
            acomodador.join();
        }catch(InterruptedException exception){
            System.out.println("No se pudieron sincronizar el tablero y el acomodador");
        }

        limpiarTablero();

        /* AGREGO EL NUEVO ARRAY AL PRINCIPAL */
        point.addAll(celulasobrevivientes);

        repaint();

        /* TODO: Agregar aca una marca de tiempo para medir el tiempo total de ejecucion */
        long tiempoFinal = System.currentTimeMillis() - tiempoInicio;

        try {
            /* Duermo el hilo 1000 / en el contenido de movimiento por segundo --->3 */
            Thread.sleep(1000 / i_movesPerSecond);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
            /* TODO: En caso de que no se pare el hilo. Como lo puedo parar a la fuerza? */
        } finally {
            System.out.printf("El tiempo final para el ciclo parcial es de: %f milisegundos\n",(float) tiempoFinal);

            /* Dejo correr */
            run();
        }
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
